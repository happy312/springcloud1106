package com.niuniu.user.feignclient;

import com.niuniu.common.config.DefaultFeignConfig;
import com.niuniu.common.vo.Response;
import com.niuniu.user.feignclient.fallback.OrderClientFallBackFactory;
import com.niuniu.user.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(value = "order-service", configuration = DefaultFeignConfig.class)
public interface OrderClient {
    @GetMapping(value = "/order-service/order/getOrdersByUserId")
    List<Order> getOrdersByUserId(@RequestParam("userId") Long userId);

    @PostMapping(value = "/order-service/order/createOrder")
    Response createOrder(@RequestParam("productId") Long productId, @RequestParam("num") Integer num);
}
