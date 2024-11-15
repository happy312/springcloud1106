package com.niuniu.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @TableId
    @TableField(value = "order_id")
    private Long orderId;
    private Double price;
    @TableField(value = "create_time")
    private Date createTime;
    private String address;
    @TableField(value = "user_id")
    private Long userId;
}
