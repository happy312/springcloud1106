package com.niuniu.order.service.impl;

import com.niuniu.order.mapper.OrderMapper;
import com.niuniu.order.model.Order;
import com.niuniu.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public String hello() {
        return "你好，这里是order-service";
    }

    @Transactional
    @Override
    public Order createOrder(Long userId) {
        Order order = new Order(userId, Double.valueOf(100), new Date(), "北京市", userId);
        orderMapper.createOrder(order);

        return order;
    }
}
