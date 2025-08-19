package com.niuniu.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.niuniu.common.vo.Response;
import com.niuniu.order.model.Order;

public interface OrderService extends IService<Order> {
    String hello();

    Response createOrder(Long productId, Integer num);

    /**
     * 100个人抢三张券，每人限购一张
     * @return
     */
    Response dealCoupon(Long productId, Integer num);
}
