package com.niuniu.order.feignclient.fallback;

import com.niuniu.common.vo.Response;
import com.niuniu.order.feignclient.ProductStoreClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductStoreClientFallBackFactory implements FallbackFactory<ProductStoreClient> {

    @Override
    public ProductStoreClient create(Throwable throwable) {

        return new ProductStoreClient() {
            @Override
            public Response updateStockById(Long productId, Integer num) {
                log.error("更新商品库存失败，服务降级。productId = " + productId);
                return null;
            }

            @Override
            public Response getStockById(Long productId) {
                log.error("查询商品库存失败，服务降级。productId = " + productId);
                return null;
            }
        };
    }
}
