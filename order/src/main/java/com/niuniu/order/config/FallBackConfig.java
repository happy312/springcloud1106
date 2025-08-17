package com.niuniu.order.config;

import com.niuniu.order.feignclient.fallback.ProductClientFallBackFactory;
import com.niuniu.order.feignclient.fallback.ProductStoreClientFallBackFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FallBackConfig {

    @Bean
    public ProductStoreClientFallBackFactory productStoreClientFallBackFactory() {
        return new ProductStoreClientFallBackFactory();
    }

    @Bean
    public ProductClientFallBackFactory productClientFallBackFactory() {
        return new ProductClientFallBackFactory();
    }
}
