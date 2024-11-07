package com.niuniu.order.controller;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import com.niuniu.order.mapper.OrderMapper;
import com.niuniu.order.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 手写负载均衡
     * 调用user-service
     * @return
     */
    @RequestMapping("/hello")
    public String hello(){
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
        ServiceInstance serviceInstance = instances.get(RandomUtil.randomInt(instances.size()));
        URI uri = serviceInstance.getUri();

        String url = uri + "/user-service/user/hello";
        log.info("调用地址：" + url);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<String>() {
                },
                String.class);

        String msg = result.getBody();
        log.info("接口返回：" + msg);
        return msg;
    }

    @GetMapping("/queryOrderByIds")
    public List<Order> queryOrderByIds(@RequestParam(name = "ids") List<Long> ids){

        return Lists.newArrayList(orderMapper.getById(1L));
    }
}
