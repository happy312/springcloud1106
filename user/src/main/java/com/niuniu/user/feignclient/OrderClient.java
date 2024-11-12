package com.niuniu.user.feignclient;

import com.niuniu.common.config.DefaultFeignConfig;
import com.niuniu.user.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "order-service", configuration = DefaultFeignConfig.class)
public interface OrderClient {
    @GetMapping(value = "/order-service/order/queryOrderByIds")
    List<Order> queryOrderByIds(@RequestParam("ids") List<Long> ids);
}
