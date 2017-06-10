package com.xfl.boot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @RequestMapping(value = "/boot")
    public Map<String,Object> test(HttpServletRequest servletRequest, HttpServletResponse servletResponse){
        System.out.println("Test");
        Map<String,Object> result = new HashMap<>();
        result.put("msg","test");
        return result;
    }
}
