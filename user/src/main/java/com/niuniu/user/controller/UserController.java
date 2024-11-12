package com.niuniu.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.niuniu.common.CommonConstant;
import com.niuniu.common.utils.UserContext;
import com.niuniu.user.feignclient.OrderClient;
import com.niuniu.user.mapper.UserMapper;
import com.niuniu.user.model.Order;
import com.niuniu.user.model.User;
import com.niuniu.user.util.JWTUtil;
import com.niuniu.user.vo.Response;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderClient orderClient;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 手写负载均衡
     * 调用user-service
     * @return
     */
    @RequestMapping("/hello")
    public String hello(){

        return "hello，这里是user-service";
    }

    @GetMapping("/queryOrderByIds")
    public List<User> queryOrderByIds(@RequestParam(name = "ids") List<String> ids){

        return Collections.emptyList();
    }

    /**
     * 登录成功给前端返回token
     * @param name
     * @param password
     * @return
     */
    @GetMapping("/login")
    public Response login(@RequestParam(name = "name") String name, @RequestParam(name = "password") String password){
        // 1、参数是否合法
        if (StringUtil.isEmpty(name) || StringUtil.isEmpty(password)) {
            return Response.fail("用户名或密码不能为空");
        }
        // 2、用户是否存在
        User user = userMapper.login(name, password);
        // 3、用户不存在，登录失败
        if (user == null) {
            return Response.fail("用户不存在");
        }
        // 4、用户存在，使用jwt生成token返给前端
        String token = JWTUtil.createToken(user.getId());
        // 5、将token放入redis，设置有效期限为1分钟。
        String key = "token_" + token;
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(user),
                System.currentTimeMillis() + 5 * 60 * 1000L, TimeUnit.MILLISECONDS);
        return Response.ok(token);
    }

    /**
     * 前端传递token过来，根据token查询用户
     * @param token
     * @return
     */
    @GetMapping("/findUserByToken")
    public Response findUserByToken(@RequestParam(name = "token") String token) {
        if (StringUtil.isEmpty(token)) {
            return Response.fail("无效的token");
        }
        Map<String, Object> map = JWTUtil.checkToken(token);
        if (map == null) {
            return Response.fail("无效的token");
        }
        String userJson = redisTemplate.opsForValue().get("token_" + token);
        if (StringUtil.isEmpty(userJson)) {
            return Response.fail("无效的token");
        }
        User user = JSONObject.parseObject(userJson, User.class);

        return Response.ok(user);
    }

    @GetMapping("/getCurrentUser")
    public Response getCurrentUser(HttpServletRequest request) {
        System.out.println("是否从UserContext获取到了用户信息 = " + UserContext.getUser());

        String userInfoo = request.getHeader(CommonConstant.userInfo);
        if (StringUtil.isEmpty(userInfoo)) {
            return Response.fail("getCurrentUser error!");
        }

        return Response.ok();
    }

    /**
     * 退出登录，清楚redis中存储的token信息
     * @param token
     * @return
     */
    @GetMapping("/logout")
    public Response logout(@RequestParam(name = "token") String token) {
        redisTemplate.delete("token_" + token);
        return Response.ok();
    }

    /**
     * 查询用户的订单列表
     * @param
     * @return
     */
    @GetMapping("/getOrderList")
    public Response getOrderList() {
        List<Order> orders = orderClient.queryOrderByIds(Lists.newArrayList(UserContext.getUser()));
        return Response.ok(orders);
    }

}
