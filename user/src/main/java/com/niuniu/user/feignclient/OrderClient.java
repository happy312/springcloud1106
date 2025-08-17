package com.niuniu.user.feignclient;

import com.niuniu.common.config.DefaultFeignConfig;
import com.niuniu.user.feignclient.fallback.OrderClientFallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient(value = "order-service", configuration = DefaultFeignConfig.class, fallbackFactory = OrderClientFallBackFactory.class)
public interface OrderClient {

}
