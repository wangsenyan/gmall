package com.dubbo.service;

import com.dubbo.bean.UserAddress;

import java.util.List;

/**
 * 1. 将服务提供者注册到注册中心
 *   1） 导入dubbo依赖
 *   2)
 * 2. 让服务消费者去注册中心订阅服务提供者的服务地址
 */
public interface UserService {
    public List<UserAddress> getUserAddress(String userId);
}
