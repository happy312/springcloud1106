package com.niuniu.user.config;

import com.niuniu.user.feignclient.fallback.OrderClientFallBackFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FallBackConfig {

    @Bean
    public OrderClientFallBackFactory orderClientFallBackFactory() {
        return new OrderClientFallBackFactory();
    }
}
