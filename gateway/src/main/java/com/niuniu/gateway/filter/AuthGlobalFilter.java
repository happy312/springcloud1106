package com.niuniu.gateway.filter;

import com.niuniu.common.CommonConstant;
import com.niuniu.gateway.config.AuthProperties;
import com.niuniu.gateway.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1、获取request
        ServerHttpRequest request = exchange.getRequest();
        // 2、判断是否需要做登录拦截
        if (isExclude(request.getPath().toString())) {
            // 放行
            return chain.filter(exchange);
        }
        // 3、从请求中获取token
        String token = null;
        List<String> heads = request.getHeaders().get("token");
        if (heads != null && !heads.isEmpty()) {
            token = heads.get(0);
        }
        // 4、校验并解析token
        Long userId = JWTUtil.parseToken(token);
        if (userId == null) { // 解析失败
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        // 如果登录超时，需要重新登录
        String key = CommonConstant.TOKEN_ + token;
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) { // 登录超时
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 5、传递用户信息
        ServerWebExchange swe = exchange.mutate()
                .request(builder -> builder.header(CommonConstant.userInfo, userId.toString()))
                .build();
        System.out.println("userId = " + userId);

        // 6、放行
        return chain.filter(swe);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Boolean isExclude(String path) {
        for (String excluedePath : authProperties.getExcludePaths()) {
           if ( antPathMatcher.match(excluedePath, path)) {
               return true;
           }
        }
        return false;
    }
}
