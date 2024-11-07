package com.niuniu.order.service.impl;

import com.niuniu.order.service.OrderService;
import org.springframework.cloud.client.discovery.DiscoveryClient;

public class OrderServiceImpl implements OrderService {

    @Override
    public String hello() {


        return "你好，这里是order-service";
    }
}
