package com.xfl.boot.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Policy;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.xfl.boot.Service.UserService;
import com.xfl.boot.bean.UserVo;
import com.xfl.boot.common.utils.CryptAESAndRSAUtils;
import com.xfl.boot.common.utils.ramq.HelloSender;
import com.xfl.boot.entity.Emplyee;
import com.xfl.boot.entity.TestEntity;
import com.xfl.boot.entity.User;
import com.xfl.boot.interceptor.RateLimit;
import com.xfl.boot.provider.service.IDemoService;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
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
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by XFL
 * time on 2017/6/6 21:59
 * description:
 */
@RateLimit
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

    public static void main(String[] args) {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "8");
        Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(1)
                //写之后1min过期
                .expireAfterWrite(1, TimeUnit.SECONDS)
                //访问之后1min过期
                .expireAfterAccess(1,TimeUnit.SECONDS)
//                //20ms之后刷新
//                .refreshAfterWrite(20, TimeUnit.SECONDS)
                //开启weakKey key 当启动垃圾回收时，该缓存也被回收
                .weakKeys()
                //指定线程池
                .executor(Executors.newFixedThreadPool(3))
                .build();
        cache.put("hello1","hello1");
        cache.put("hello2","hello2");
        cache.put("hello3","hello3");
        cache.put("hello4","hello4");
        System.out.println(cache.getIfPresent("hello1"));
        System.out.println(cache.getIfPresent("hello4"));
        System.out.println(cache.estimatedSize());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(cache.estimatedSize());
        System.out.println(cache.getIfPresent("hello1"));
        System.out.println(cache.getIfPresent("hello2"));
        System.out.println(cache.getIfPresent("hello3"));
        System.out.println(cache.getIfPresent("hello4"));
        System.out.println(cache.estimatedSize());
        System.out.println("CPU Core: " + Runtime.getRuntime().availableProcessors());
        System.out.println("CommonPool Parallelism: " + ForkJoinPool.commonPool().getParallelism());
        System.out.println("CommonPool Common Parallelism: " + ForkJoinPool.getCommonPoolParallelism());
        long start = System.nanoTime();
    }


}
