package com.niuniu.product.controller;

import com.google.common.collect.Lists;
import com.niuniu.product.feignclient.OrderClient;
import com.niuniu.product.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    private OrderClient orderClient;
    /**
     * 手写负载均衡
     * 调用user-service
     * @return
     */
    @RequestMapping("/testFeign")
    public List<Order> testFeign(){
        List<Order> orders = orderClient.queryOrderByIds(Lists.newArrayList(1L, 2L));
        return orders;
    }
}
