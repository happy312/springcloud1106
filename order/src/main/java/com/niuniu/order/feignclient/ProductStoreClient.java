package com.niuniu.order.feignclient;

import com.niuniu.common.config.DefaultFeignConfig;
import com.niuniu.common.vo.Response;
import com.niuniu.order.feignclient.fallback.ProductStoreClientFallBackFactory;
import com.niuniu.order.vo.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "store-service", configuration = DefaultFeignConfig.class, fallbackFactory = ProductStoreClientFallBackFactory.class)
public interface ProductStoreClient {

    @GetMapping(value = "/store-service/productStore/updateStockById")
    Response updateStockById(@RequestParam("productId") Long productId, @RequestParam("num") Integer num);
}
