package com.xfl.boot.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by XFL
 * time on 2017/6/11 21:12
 * description:dubbo相关配置文件引入
 */
@Configuration
@PropertySource("classpath:dubbo/config/dubbo.properties")
@ImportResource("classpath:dubbo/dubbo-consumer.xml")
public class DubboConfig {
}
