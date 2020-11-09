package com.dubbo.service;

import com.dubbo.bean.UserAddress;

import java.util.List;

public interface OrderService {
    public List<UserAddress> initOrder(String userId);
}
