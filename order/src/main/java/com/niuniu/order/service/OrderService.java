package com.niuniu.order.service;

import com.niuniu.order.model.Order;

public interface OrderService {
    String hello();

    Order createOrder(Long userId);
}
