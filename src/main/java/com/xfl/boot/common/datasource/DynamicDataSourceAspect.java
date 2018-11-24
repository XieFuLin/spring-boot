package com.xfl.boot.common.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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

    @Around("execution(* com.xfl.boot.dao.*.*(..))")
//    @Around("@within(com.xfl.boot.common.datasource.TargetDataSource)||@annotation(com.xfl.boot.common.datasource.TargetDataSource)")
    public Object pointcut(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Class<?> target = joinPoint.getTarget().getClass();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            TargetDataSource annotation = null;
            Method method = signature.getMethod();
            //获取方法上的注解
            annotation = method.getAnnotation(TargetDataSource.class);
            // 默认使用目标类型的注解，如果没有则使用其实现接口的注解类
            if (annotation == null) {
                for (Class<?> cls : target.getInterfaces()) {
                    annotation = cls.getAnnotation(TargetDataSource.class);
                    if (annotation != null) {
                        break;
                    }
//               resetDataSource(cls, signature.getMethod());
                }
            }
            if (annotation != null) {
                DynamicDataSourceContextHolder.setDataSource(annotation.name());
            }
//           for (Class<?> cls : target.getInterfaces()) {
//               TargetDataSource annotation = cls.getAnnotation(TargetDataSource.class);
//               if(annotation != null){
//                   break;
//               }
////               resetDataSource(cls, signature.getMethod());
//           }
//           resetDataSource(target, signature.getMethod());
            return joinPoint.proceed();
        } finally {
            DynamicDataSourceContextHolder.clear();
        }
    }

    /**
     * 提取目标对象方法注解和类注解中的数据源标识
     */
    private void resetDataSource(Class<?> cls, Method method) throws Exception {
        try {
            Class<?>[] types = method.getParameterTypes();
            // 默认使用类注解
            if (cls.isAnnotationPresent(TargetDataSource.class)) {
                TargetDataSource source = cls.getAnnotation(TargetDataSource.class);
                DynamicDataSourceContextHolder.setDataSource(source.name());
            }
            // 方法注解可以覆盖类注解
            Method m = cls.getMethod(method.getName(), types);
            if (m != null && m.isAnnotationPresent(TargetDataSource.class)) {
                TargetDataSource source = m.getAnnotation(TargetDataSource.class);
                DynamicDataSourceContextHolder.setDataSource(source.name());
            }
        } catch (Exception e) {
            logger.error("resetDataSource exception error={}", e);
            throw e;
        }
    }
}
