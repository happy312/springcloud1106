package com.niuniu.user.service.impl;

import com.niuniu.user.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public String hello() {
        return "你好，这里是user-service";
    }
}
