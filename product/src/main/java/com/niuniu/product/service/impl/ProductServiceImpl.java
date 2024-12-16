package com.niuniu.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.niuniu.product.mapper.ProductMapper;
import com.niuniu.product.model.Product;
import com.niuniu.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Transactional
    @Override
    public Integer createProduct(Long id) {
        return this.baseMapper.insert(new Product(id, "111", new BigDecimal("100"), 10000, new Date()));
    }

    @Override
    public Integer updateStockById(Long productId, Integer num) {
        Product product = this.baseMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在！");
        }
        Integer stockNum = product.getStock() - num;
        if (stockNum < 0) {
            throw new RuntimeException("库存不足！");
        }
        lambdaUpdate().set(Product::getStock, stockNum)
                .eq(Product::getProductId, productId)
                .eq(Product::getStock, product.getStock()) // 乐观锁
                .update();
        return null;
    }
}
