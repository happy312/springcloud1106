package com.niuniu.user.feignclient.fallback;

import com.niuniu.user.feignclient.OrderClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderClientFallBackFactory implements FallbackFactory<OrderClient> {
    @Override
    public OrderClient create(Throwable throwable) {
        log.error("OrderClient error!", throwable);
        return null;
    }
}
