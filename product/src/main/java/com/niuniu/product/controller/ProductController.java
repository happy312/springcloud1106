package com.niuniu.product.controller;

import com.google.common.collect.Lists;
import com.niuniu.common.vo.Response;
import com.niuniu.product.feignclient.OrderClient;
import com.niuniu.product.model.Order;
import com.niuniu.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private ProductService productService;
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

    @RequestMapping("/createProduct")
    public Response createProduct(@RequestParam(name = "id") Long id){
        productService.createProduct(id);
        return Response.ok();
    }

    @RequestMapping("/updateStockById")
    public Response updateStockById(@RequestParam(name = "productId") Long productId, @RequestParam(name = "num") Integer num){
        productService.updateStockById(productId, num);
        return Response.ok();
    }

    @RequestMapping("/getProductById")
    public Response getProductById(@RequestParam(name = "productId") Long productId){
        return Response.ok(productService.getById(productId));
    }
}
