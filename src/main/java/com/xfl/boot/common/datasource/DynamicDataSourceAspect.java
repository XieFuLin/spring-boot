package com.xfl.boot.common.datasource;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by XFL
 * time on 2018/11/18 23:39
 * description: 保证在@Transactional之前执行
 */
@Aspect
@Order(-10)
@Component
public class DynamicDataSourceAspect {
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    //@within在类上设置
    //@annotation在方法上进行设置
    @Pointcut("@within(com.xfl.boot.common.datasource.TargetDataSource)||@annotation(com.xfl.boot.common.datasource.TargetDataSource)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        TargetDataSource annotationClass = method.getAnnotation(TargetDataSource.class);//获取方法上的注解
        if (annotationClass == null) {
            annotationClass = joinPoint.getTarget().getClass().getAnnotation(TargetDataSource.class);//获取类上面的注解
            if (annotationClass == null) {
                return;
            }
        }
        //获取注解上的数据源的值的信息
        String dataSourceKey = annotationClass.name();
        if (StringUtils.isNotBlank(dataSourceKey)) {
            //给当前的执行SQL的操作设置特殊的数据源的信息
            DynamicDataSourceContextHolder.setDataSource(dataSourceKey);
        }
        logger.info("DynamicDataSourceAspect，dataSource change={}", dataSourceKey);
    }

    @After("pointcut()")
    public void after(JoinPoint point) {
        //清理掉当前设置的数据源，让默认的数据源不受影响
        DynamicDataSourceContextHolder.clear();
    }
}
