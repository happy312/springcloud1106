package com.niuniu.product.feignclient;

import com.niuniu.product.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "order-service")
public interface OrderClient {
    @GetMapping(value = "/order-service/order/queryOrderByIds")
    List<Order> queryOrderByIds(@RequestParam("ids") List<Long> ids);
}
