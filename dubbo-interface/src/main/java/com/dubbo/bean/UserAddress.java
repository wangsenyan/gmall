package com.dubbo.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserAddress implements Serializable {
    private Integer id;
    private String name;

    public UserAddress(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
