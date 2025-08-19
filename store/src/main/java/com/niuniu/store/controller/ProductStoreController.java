package com.niuniu.store.controller;

import com.niuniu.common.vo.Response;
import com.niuniu.store.service.ProductStoreService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/productStore")
public class ProductStoreController {

    @Resource
    private ProductStoreService productStoreService;

    @RequestMapping("/updateStockById")
    public Response updateStockById(@RequestParam(name = "productId") Long productId, @RequestParam(name = "num") Integer num){
        productStoreService.updateStockById(productId, num);
        return Response.ok();
    }

    @RequestMapping("/getStockById")
    public Response getStockById(@RequestParam(name = "productId") Long productId){
        return Response.ok(productStoreService.getStockById(productId));
    }
}
