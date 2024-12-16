package com.niuniu.product.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_product")
public class Product {
    @TableId
    @TableField(value = "product_id")
    private Long productId;

    @TableField(value = "product_name")
    private String productName;

    @TableField(value = "price")
    private BigDecimal price;

    @TableField(value = "stock")
    private Integer stock;

    @TableField(value = "update_time")
    private Date updateTime;
}
