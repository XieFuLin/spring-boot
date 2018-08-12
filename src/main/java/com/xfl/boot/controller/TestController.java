package com.xfl.boot.controller;

import com.xfl.boot.Service.UserService;
import com.xfl.boot.bean.UserVo;
import com.xfl.boot.common.utils.CryptAESAndRSAUtils;
import com.xfl.boot.common.utils.ramq.HelloSender;
import com.xfl.boot.entity.Emplyee;
import com.xfl.boot.entity.TestEntity;
import com.xfl.boot.entity.User;
import com.xfl.boot.provider.service.IDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XFL
 * time on 2017/6/6 21:59
 * description:
 */
@Validated
@RestController
@RequestMapping(value = "/test")
public class TestController {
    @Resource
    private IDemoService demoService;

    @Autowired
    private HelloSender helloSender;
    @Autowired
    private UserService userService;
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

    /**
     * @param userName
     * @return
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<UserVo> getUserList(@RequestParam(value = "name") String userName) {
        return userService.getUserByName(userName);
    }

    @RequestMapping(value = "validation", method = RequestMethod.POST)
    public UserVo testValidation(@RequestBody @Valid UserVo userVo) {
        return userVo;
    }

    //Controller上需要增加注解@Validated
    @RequestMapping(value = "val", method = RequestMethod.GET)
    public UserVo testVal(@RequestParam("phone")
                          @Pattern(regexp = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147)|(166)|(199)|(198))\\\\d{8}$", message = "手机号码不正确")
                                  String phone) {
        return new UserVo();
    }
}
