package com.niuniu.order.feignclient;

import com.niuniu.common.config.DefaultFeignConfig;
import com.niuniu.common.vo.Response;
import com.niuniu.order.vo.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "product-service", configuration = DefaultFeignConfig.class)
public interface ProductClient {
    @GetMapping(value = "/product-service/product/getProductById")
    Response<Product> getProductById(@RequestParam("productId") Long productId);

    @GetMapping(value = "/product-service/product/updateStockById")
    Response updateStockById(@RequestParam("productId") Long productId, @RequestParam("num") Integer num);
}
