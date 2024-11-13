package com.niuniu.order.mapper;

import com.niuniu.order.model.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Select("select order_id, price, create_time, address, user_id from t_order where order_id = #{orderId}")
    Order getById(@Param("orderId") Long orderId);

    @Select("select order_id, price, create_time, address, user_id from t_order where user_id = #{userId}")
    List<Order> getByUserId(@Param("userId") Long userId);
}
