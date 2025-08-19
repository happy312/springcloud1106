package com.niuniu.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.niuniu.store.mapper.ProductStoreMapper;
import com.niuniu.store.model.ProductStore;
import com.niuniu.store.service.ProductStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@Service
public class ProductStoreServiceImpl extends ServiceImpl<ProductStoreMapper, ProductStore> implements ProductStoreService {

    @Resource
    private ProductStoreMapper productStoreMapper;

    @Override
    public Boolean updateStockById(Long productId, Integer num) {
        ProductStore productStore = productStoreMapper.selectById(productId);
        if (Objects.isNull(productStore)) {
            throw new RuntimeException("该商品库存不存在，productId = " + productId);
        }

        return lambdaUpdate().set(ProductStore::getStock, productStore.getStock() - num)
                .eq(ProductStore::getProductId, productId)
                .eq(ProductStore::getStock, productStore.getStock()) // 乐观锁
                .update();
    }

    @Override
    public Integer getStockById(Long productId) {
        return productStoreMapper.selectById(productId).getStock();
    }
}
