package com.xfl.boot.controller;

import com.xfl.boot.common.utils.CryptAESAndRSAUtils;
import com.xfl.boot.common.utils.ramq.HelloSender;
import com.xfl.boot.entity.Emplyee;
import com.xfl.boot.entity.TestEntity;
import com.xfl.boot.entity.User;
import com.xfl.boot.provider.service.IDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
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

    @Autowired
    private HelloSender helloSender;

    @Value("${rsa.publickey}")
    private String publicKey;
    @RequestMapping(value = "/boot")
    public Map<String,Object> test(HttpServletRequest servletRequest, HttpServletResponse servletResponse){
        System.out.println("Test");
        Map<String,Object> result = new HashMap<>();
        result.put("msg","test");
        result.put("code", 12);
        CryptAESAndRSAUtils.testSyncOrder();
        try (BufferedReader reader = servletRequest.getReader()) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        //   System.out.println(demoService.sayHello("Boot Test"));
        return result;
    }

    @RequestMapping(value = "/date", method = RequestMethod.POST)
    public TestEntity test2(@RequestBody User param) {
        System.out.println(param.getDesc());
        TestEntity testEntity = new TestEntity();
        testEntity.setDate(new Date());
        testEntity.setDate2(new Date());
        return testEntity;
    }

    @RequestMapping(value = "/decrypt", method = RequestMethod.POST)
    public Map<String, String> testDecrypt(@RequestBody Map<String, String> param) {
        System.out.println("Test");
        Map<String, String> result = new HashMap<>();
        result.put("msg", "test");
        CryptAESAndRSAUtils.decryptData(param, publicKey);
        return result;
    }

    @RequestMapping(value = "/mq", method = RequestMethod.GET)
    public String testMq() {
        String result = "success";
        helloSender.sendFanout();
        return result;
    }

    @RequestMapping(value = "/empy", method = RequestMethod.POST)
    public Emplyee saveEmployee(@RequestBody Emplyee empy) {
        return empy;
    }
}
