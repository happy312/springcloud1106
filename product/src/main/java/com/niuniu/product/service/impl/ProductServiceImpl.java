package com.niuniu.product.service.impl;

import com.niuniu.product.mapper.ProductMapper;
import com.niuniu.product.model.Product;
import com.niuniu.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Transactional
    @Override
    public Integer createProduct(Long id) {
        return productMapper.createProduct(new Product(id, "111", Double.valueOf(100)));
    }
}
