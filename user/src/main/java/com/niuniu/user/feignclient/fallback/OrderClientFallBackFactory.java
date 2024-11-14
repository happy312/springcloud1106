package com.niuniu.user.feignclient.fallback;

import com.niuniu.user.feignclient.OrderClient;
import com.niuniu.user.model.Order;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
public class OrderClientFallBackFactory implements FallbackFactory<OrderClient> {
    @Override
    public OrderClient create(Throwable throwable) {
        log.error("OrderClient error!", throwable);
        return new OrderClient() {
            @Override
            public List<Order> getOrdersByUserId(Long userId) {
                return Collections.emptyList();
            }
        };
    }
}
