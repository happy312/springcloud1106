package com.niuniu.order.service.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MyServiceFactory2 {
    /**
     * key是bean名称
     */
    private final Map<String, PayService> payServiceMap;

    @Autowired
    public MyServiceFactory2(Map<String, PayService> payServiceMap) {
        this.payServiceMap = payServiceMap;
    }

    public PayService getPayService(String beanName){
        return payServiceMap.get(beanName);
    }
}
