package com.xfl.boot.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Created by XFL
 * time on 2017/8/16 23:50
 * description:
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class User {
    private String name;
    private String age;
    private String desc;
    //将Long类型系列化成String避免精度丢失
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", desc='" + desc + '\'' +
                ", userId=" + userId +
                '}';
    }
}
