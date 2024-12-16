package com.niuniu.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.niuniu.product.model.Product;

public interface ProductService extends IService<Product> {

    Integer createProduct(Long id);

    Integer updateStockById(Long productId, Integer num);
}
