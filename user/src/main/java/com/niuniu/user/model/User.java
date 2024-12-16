package com.niuniu.user.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user")
public class User implements Serializable {
    @TableId
    private Long id;

    private String name;

    private String password;

    @TableField(value = "update_time")
    private Date updateTime;
}
