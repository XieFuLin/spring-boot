package com.xfl.boot.interceptor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 *
 * @author xiefulin
 * @time 2018年4月13日 下午4:55:44
 * @description:接口访问次数限制 默认一分钟300次
 */
public @interface RateLimit {
    int timeOutSeconds() default 60;

    int maxLimitCount() default 300;
}
