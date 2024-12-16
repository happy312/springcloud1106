package com.niuniu.user.service.impl;

import com.niuniu.user.feignclient.OrderClient;
import com.niuniu.user.feignclient.ProductClient;
import com.niuniu.user.mapper.UserMapper;
import com.niuniu.user.model.User;
import com.niuniu.user.service.UserService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final OrderClient orderClient;

    private final ProductClient productClient;

    @Override
    public String hello() {
        return "你好，这里是user-service";
    }

    @GlobalTransactional
    @Override
    public String testSeataXA() {
        log.info("RootContext.ID:{}", RootContext.getXID());
        Long id = 16L;

        // 1、调用order-service服务
        orderClient.createOrder();

        // 2、调用product-service服务
        productClient.createProduct(id);

        // 3.本服务（user-service）的业务修改
        userMapper.insertUser(new User(id, "111", "111111"));

        log.info("执行结束");
        try {
            Thread.sleep(60*1000);
        } catch (Exception e){
            e.printStackTrace();
        }

        /*int i = 1;
        try{
            System.out.println(i/0);
        } catch (Exception e) {
            throw new RuntimeException();
        }*/
        return null;
    }
}
