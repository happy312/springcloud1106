package com.niuniu.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.niuniu.order.model.Order;
import com.niuniu.order.model.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}
