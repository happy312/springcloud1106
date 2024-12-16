package com.niuniu.order.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
