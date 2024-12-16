package com.niuniu.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.niuniu.common.utils.UserContext;
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

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final OrderClient orderClient;

    @Override
    public String hello() {
        return "你好，这里是user-service";
    }

    @GlobalTransactional
    @Override
    public String testSeataXA(Long productId, Integer num) {
        User user = this.getById(UserContext.getUser());

        // 1、调用order-service服务(order-service会调用product_service扣减库存)
        orderClient.createOrder(productId, num);

        // 2.本服务（user-service）的业务修改
        user.setUpdateTime(new Date());
        this.baseMapper.updateById(user);

        log.info("执行结束");

        int i = 0;
//        System.out.println(100/i);
        return null;
    }
}
