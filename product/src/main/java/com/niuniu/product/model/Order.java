package com.niuniu.product.model;

import lombok.Data;

import java.sql.Driver;
import java.util.Date;

@Data
public class Order {
    private Long orderId;
    private Double price;
    private Date createTime;
    private String address;
    private Long userId;

    public static void main(String[] args) {
        Driver driver = null;
        driver.getMinorVersion();
//        driver.acceptsURL()
    }
}
