package com.dubbo.service.impl;

import com.dubbo.bean.UserAddress;

import com.dubbo.service.OrderService;
import com.dubbo.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
@Component
public class OrderServiceImpl implements OrderService {

    //代替@Autowire
    @DubboReference(retries = 2,timeout = 6000,version = "*",stub = "com.dubbo.service.impl.UserServiceStub",loadbalance = "roundrobin")
    UserService userService;
    @HystrixCommand(fallbackMethod = "hello")
    @Override
    public List<UserAddress> initOrder(String userId) {
        return userService.getUserAddress(userId);
    }
    public List<UserAddress> hello(String userId){
        return Arrays.asList(new UserAddress(0,"dad"));
    }
}
