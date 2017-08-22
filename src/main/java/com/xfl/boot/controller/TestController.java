package com.xfl.boot.controller;

import com.xfl.boot.entity.TestEntity;
import com.xfl.boot.entity.User;
import com.xfl.boot.provider.service.IDemoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by XFL
 * time on 2017/6/6 21:59
 * description:
 */
@RestController
@RequestMapping(value = "/test")
public class TestController {
    @Resource
    private IDemoService demoService;
    @RequestMapping(value = "/boot")
    public Map<String,Object> test(HttpServletRequest servletRequest, HttpServletResponse servletResponse){
        System.out.println("Test");
        Map<String,Object> result = new HashMap<>();
        result.put("msg","test");
        result.put("code", 12);
        //   System.out.println(demoService.sayHello("Boot Test"));
        return result;
    }

    @RequestMapping(value = "/date")
    public TestEntity test2(@RequestBody User param) {
        System.out.println(param.getDesc());
        TestEntity testEntity = new TestEntity();
        testEntity.setDate(new Date());
        testEntity.setDate2(new Date());
        return testEntity;
    }
}
