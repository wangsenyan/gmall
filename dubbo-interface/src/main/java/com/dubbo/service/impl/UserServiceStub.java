package com.dubbo.service.impl;

import com.dubbo.bean.UserAddress;
import com.dubbo.service.UserService;
import io.netty.util.internal.StringUtil;

import java.util.List;

public class UserServiceStub implements UserService {

    private final UserService userService;

    /**
     * 传入的是UserService的远程代理对象
     * 先调用本地存根,再调用远程实现
     * @param userService
     */
    public UserServiceStub(UserService userService) {
        super();
        this.userService = userService;
    }

    @Override
    public List<UserAddress> getUserAddress(String userId) {
        System.out.println("本地存根");
        if(!StringUtil.isNullOrEmpty(userId))
          return  userService.getUserAddress(userId);
        return null;
    }
}
