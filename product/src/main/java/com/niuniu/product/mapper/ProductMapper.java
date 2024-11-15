package com.niuniu.product.mapper;

import com.niuniu.product.model.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper {

    @Insert("insert into t_product(product_id, product_name, price) values(#{product.productId}, #{product.productName}, #{product.price}) ")
    Integer createProduct(@Param("product") Product product);
}
