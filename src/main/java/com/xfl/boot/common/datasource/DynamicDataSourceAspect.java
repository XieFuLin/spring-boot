package com.xfl.boot.common.datasource;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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

}
