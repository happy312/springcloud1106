package com.niuniu.product.config;


import feign.Logger;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {

    @Bean
    public Logger.Level feignLogLevel(){
        return Logger.Level.FULL;
    }

}
