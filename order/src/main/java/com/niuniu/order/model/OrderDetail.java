package com.niuniu.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_order_detail")
public class OrderDetail {

    @TableId
    @TableField(value = "order_detail_id")
    private String orderDetailId;

    @TableField(value = "order_id")
    private String orderId;

    @TableField(value = "product_id")
    private Long productId;

    private BigDecimal price;

    private Integer num;
}
