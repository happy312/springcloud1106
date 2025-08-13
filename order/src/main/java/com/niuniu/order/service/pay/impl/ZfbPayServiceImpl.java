package com.niuniu.order.service.pay.impl;


import com.niuniu.order.service.pay.PayService;
import org.springframework.stereotype.Service;

@Service
public class ZfbPayServiceImpl implements PayService {
    @Override
    public String pay() {

        return "zfb";
    }
}
