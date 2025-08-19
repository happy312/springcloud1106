package com.niuniu.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.niuniu.store.model.ProductStore;

public interface ProductStoreService extends IService<ProductStore> {

    Boolean updateStockById(Long productId, Integer num);

    Integer getStockById(Long productId);
}
