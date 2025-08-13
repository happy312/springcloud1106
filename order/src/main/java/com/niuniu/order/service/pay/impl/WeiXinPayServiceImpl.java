package com.niuniu.order.service.pay.impl;


import com.niuniu.order.service.pay.PayService;
import org.springframework.stereotype.Service;

@Service
public class WeiXinPayServiceImpl implements PayService {
    @Override
    public String pay() {
        return "weixin";
    }
}
