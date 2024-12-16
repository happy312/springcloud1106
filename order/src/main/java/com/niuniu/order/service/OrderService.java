package com.niuniu.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.niuniu.order.model.Order;

public interface OrderService extends IService<Order> {
    String hello();

    Order createOrder(Long productId, Integer num);
}
