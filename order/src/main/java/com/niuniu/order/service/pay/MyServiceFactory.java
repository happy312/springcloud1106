package com.niuniu.order.service.pay;

import com.niuniu.order.service.pay.impl.WeiXinPayServiceImpl;
import com.niuniu.order.service.pay.impl.ZfbPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class MyServiceFactory {
    @Autowired
    private ApplicationContext applicationContext;

    public PayService getPayService(String beanName){
        switch (beanName){
            case ("zfbPayServiceImpl") :
                return applicationContext.getBean(ZfbPayServiceImpl.class);
            case ("weiXinPayServiceImpl") :
                return applicationContext.getBean(WeiXinPayServiceImpl.class);
            default:
                throw new IllegalArgumentException("Unknown service type!");
        }
    }
}
