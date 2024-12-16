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
@TableName("t_order")
public class Order {

    @TableId
    @TableField(value = "order_id")
    private String orderId;

    private BigDecimal price;

    @TableField(value = "create_time")
    private Date createTime;

    private String address;

    @TableField(value = "user_id")
    private Long userId;
}
