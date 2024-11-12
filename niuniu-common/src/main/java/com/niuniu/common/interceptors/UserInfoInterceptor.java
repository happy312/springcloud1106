package com.niuniu.common.interceptors;

import com.niuniu.common.CommonConstant;
import com.niuniu.common.utils.UserContext;
import jodd.util.StringUtil;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 前端请求到网关微服务，网关微服务再映射到具体的微服务
 * 如果是微服务之间的调用，此种方式则不能传递用户信息
 */
public class UserInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userInfo = request.getHeader(CommonConstant.userInfo);
        if (StringUtil.isNotEmpty(userInfo)) {
            UserContext.setUser(Long.parseLong(userInfo));
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        UserContext.removeUser();
    }
}
