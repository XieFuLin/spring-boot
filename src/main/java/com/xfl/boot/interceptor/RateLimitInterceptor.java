package com.xfl.boot.interceptor;

import com.xfl.boot.common.exception.AppExpection;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author xiefulin
 * @time 2018年4月13日 下午5:01:58
 * @description:访问次数限制
 */
@Aspect
@Component
public class RateLimitInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RateLimitInterceptor.class);
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Before("execution(* com.xfl.boot.controller.*.*(..))")
    public void doBefore(JoinPoint joinPoint) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        RateLimit annotation = null;
        MethodSignature sign = (MethodSignature) joinPoint.getSignature();
        Method method = sign.getMethod();
        //获取方法上的注解
        annotation = method.getAnnotation(RateLimit.class);
        if (annotation == null) {
            //获取类上的注解
            annotation = joinPoint.getTarget().getClass().getAnnotation(RateLimit.class);
            if (annotation == null) {
                //获取接口上的注解
                for (Class<?> cls : joinPoint.getClass().getInterfaces()) {
                    annotation = cls.getAnnotation(RateLimit.class);
                    if (annotation != null) {
                        break;
                    }
                }
            }
        }
        // 添加注解了的需要增加访问次数限制
        if (annotation != null) {
            // 如果有session则返回session如果没有则返回null(避免创建过多的session浪费内存)
            HttpSession session = request.getSession(false);
            String ip = getIpAddress(request);
            String uri = request.getRequestURI();
            int timeOutSeconds = annotation.timeOutSeconds();
            int maxLimitCount = annotation.maxLimitCount();
            StringBuilder limitKey = new StringBuilder("access:rate:limit:");
            limitKey.append(ip).append(uri);
            Integer count = (Integer) redisTemplate.opsForValue().get(limitKey.toString());
            // 超过最大访问次数
            if (count != null && count >= maxLimitCount) {
                // 打印用户信息
                throw new AppExpection(100, "访问频繁");
            }
            if (count == null) {
                redisTemplate.opsForValue().set(limitKey.toString(), 1, timeOutSeconds, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().increment(limitKey.toString(), 1);
            }
        }
    }

    /**
     * 获取ip
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}