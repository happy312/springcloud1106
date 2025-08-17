package com.niuniu.order.feignclient.fallback;

import com.niuniu.common.vo.Response;
import com.niuniu.order.feignclient.ProductClient;
import com.niuniu.order.vo.Product;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductClientFallBackFactory implements FallbackFactory<ProductClient> {

    @Override
    public ProductClient create(Throwable throwable) {
        return new ProductClient() {
            @Override
            public Response<Product> getProductById(Long productId) {
                log.error("调用product-service服务的getProductById接口出错，服务降级！productId = " + productId);
                return null;
            }
        };
    }
}
