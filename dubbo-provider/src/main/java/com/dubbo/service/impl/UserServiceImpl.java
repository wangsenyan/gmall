package com.dubbo.service.impl;

import com.dubbo.bean.UserAddress;
import com.dubbo.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.DubboService;


import java.util.Arrays;
import java.util.List;

@DubboService(version = "1.0.0")
public class UserServiceImpl implements UserService {
    @HystrixCommand
    @Override
    public List<UserAddress> getUserAddress(String userId) {
        System.out.println("我是旧的old");
        UserAddress address1 = new UserAddress(1,"海淀区");
        UserAddress address2 = new UserAddress(1,"朝阳区");
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        if(Math.random()>0.5)
            throw new RuntimeException();
        return Arrays.asList(address1,address2);
    }
}
