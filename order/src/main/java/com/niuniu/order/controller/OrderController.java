package com.niuniu.order.controller;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import com.niuniu.common.utils.UserContext;
import com.niuniu.common.vo.Response;
import com.niuniu.order.mapper.OrderMapper;
import com.niuniu.order.model.Order;
import com.niuniu.order.service.OrderService;
import com.niuniu.order.service.impl.OrderServiceImpl;
import com.niuniu.order.service.pay.MyServiceFactory2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;
import java.util.concurrent.*;

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

    @Autowired
    private OrderService orderService;

//    @Autowired
//    private MyServiceFactory myServiceFactory;

    @Resource
    private RedisTemplate<String, String> redisTemplate;


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

    /**
     * 根据用户查询订单
     * @param userId
     * @return
     */
    @GetMapping("/getOrdersByUserId")
    public List<Order> getOrdersByUserId(@RequestParam(name = "userId") Long userId){
        log.info(UserContext.getUser().toString());
        int i = 1;
        System.out.println(i / 0);
        return orderMapper.getByUserId(userId);
    }

    /**
     * 根据订单id查询订单
     * @param ids
     * @return
     */
    @GetMapping("/queryOrderByIds")
    public List<Order> queryOrderByIds(@RequestParam(name = "ids") List<Long> ids){
        return Lists.newArrayList(orderMapper.getById(1L));
    }

    @PostMapping("/createOrder")
    public Response createOrder(@RequestParam(name = "productId") Long productId, @RequestParam(name = "num") Integer num){
        return orderService.createOrder(productId, num);
    }

    @GetMapping("/dealCoupon")
    public Response dealCoupon(){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 100, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000));
        // 100个人抢三张券，每人限购一张
        for (int i = 0; i < 100; i++) {
            /*executor.execute(()->{
                orderService.dealCoupon();
            });*/

            new Thread(()->{
                orderService.dealCoupon(1L, 1);
            }).start();
        }
        return Response.ok();
    }

    @GetMapping("/addStore")
    public Response addStore(@RequestParam(name = "productId") Long productId, @RequestParam(name = "num") Integer num){
        // 删除缓存
        redisTemplate.delete("STOCK_" + productId);
        return Response.ok();
    }

//    @Autowired
//    private MyServiceFactory myServiceFactory;
    @Autowired
    private MyServiceFactory2 myServiceFactory2;

    @PostMapping("/pay")
    public Response pay(){
        String s = myServiceFactory2.getPayService("zfbPayServiceImpl").pay();
//        String s = myServiceFactory2.getPayService("weiXinPayServiceImpl").pay();
        return Response.ok(s);
    }
}
