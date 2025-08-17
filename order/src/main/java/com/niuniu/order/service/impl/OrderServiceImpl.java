package com.niuniu.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.niuniu.common.utils.UserContext;
import com.niuniu.common.vo.Response;
import com.niuniu.order.feignclient.ProductClient;
import com.niuniu.order.feignclient.ProductStoreClient;
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
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ProductClient productClient;
    @Autowired
    private ProductStoreClient productStoreClient;

    @Override
    public String hello() {
        return "你好，这里是order-service";
    }

    /**
     * 分布式事务方法
     * @param productId
     * @param num
     * @return
     */
    @GlobalTransactional
//    @Transactional
    @Override
    public Response createOrder(Long productId, Integer num) {
        // 1、创建订单，生成订单表
        Response<Product> response = productClient.getProductById(productId); // 查询商品是否存在
        if (Objects.isNull(response)) {
            return Response.fail("调用product-service服务的getProductById接口出错，服务降级！productId = " + productId);
        }
        if (!response.getResult() || Objects.isNull(response.getBody())) {
            return Response.fail("商品不存在，创建订单失败！");
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
        // 生成订单明细表
        String detailId = UUID.randomUUID().toString();
        OrderDetail orderDetail = OrderDetail.builder()
                .orderDetailId(detailId)
                .orderId(orderId)
                .price(product.getPrice().multiply(new BigDecimal(num)))
                .productId(productId)
                .num(num)
                .build();
        orderDetailMapper.insert(orderDetail);

        // 2、调用库存微服务，减库存
        if (Objects.isNull(productStoreClient.updateStockById(productId, num))) {
            throw new RuntimeException("调用库存微服务，减库存失败！productId = " + productId);
        }

        log.debug("执行结束！");

//        int i = 0;
//        System.out.println(3/i);

        return Response.ok();
    }
}
