package com.niuniu.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.niuniu.common.utils.UserContext;
import com.niuniu.common.vo.Response;
import com.niuniu.order.feignclient.ProductClient;
import com.niuniu.order.mapper.OrderDetailMapper;
import com.niuniu.order.mapper.OrderMapper;
import com.niuniu.order.model.Order;
import com.niuniu.order.model.OrderDetail;
import com.niuniu.order.service.OrderService;
import com.niuniu.order.vo.Product;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ProductClient productClient;

    @Override
    public String hello() {
        return "你好，这里是order-service";
    }

//    @GlobalTransactional
    @Transactional
    @Override
    public Order createOrder(Long productId, Integer num) {
        // 1、创建订单
        Response<Product> response = productClient.getProductById(productId);
        if (!response.getResult()) {
            throw new RuntimeException("创建订单失败！");
        }
        Product product = response.getBody();
        Long userId = UserContext.getUser();
        String orderId = UUID.randomUUID().toString();
        Order order = Order.builder()
                .orderId(orderId)
                .createTime(new Date())
                .price(product.getPrice())
                .userId(userId)
                .address("北京市").build();
        this.baseMapper.insert(order);

        String detailId = UUID.randomUUID().toString();
        OrderDetail orderDetail = OrderDetail.builder()
                .orderDetailId(detailId)
                .orderId(orderId)
                .price(product.getPrice().multiply(new BigDecimal(num)))
                .productId(productId)
                .num(num)
                .build();
        orderDetailMapper.insert(orderDetail);

        ///2、调用库存微服务，减库存
        productClient.updateStockById(productId, num);

        log.debug("执行结束！");

        int i = 0;
//        System.out.println(100/i);
        return order;
    }
}
