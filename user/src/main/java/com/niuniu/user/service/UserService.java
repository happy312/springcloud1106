package com.niuniu.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.niuniu.user.model.User;

public interface UserService extends IService<User> {
    String hello();

    String testSeataXA(Long productId, Integer num);
}
