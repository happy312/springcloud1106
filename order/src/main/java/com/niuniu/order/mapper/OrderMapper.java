package com.niuniu.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.niuniu.order.model.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    @Select("select order_id, price, create_time, address, user_id from t_order where order_id = #{orderId}")
    Order getById(@Param("orderId") Long orderId);

    @Select("select order_id, price, create_time, address, user_id from t_order where user_id = #{userId}")
    List<Order> getByUserId(@Param("userId") Long userId);

//    @Insert("insert into t_order(order_id, price, create_time, address, user_id) values(#{order.orderId}, #{order.price}, now(), #{order.address}, #{order.userId})")
//    Integer createOrder(@Param("order") Order order);
}
