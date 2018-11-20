package com.xfl.boot.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by XFL
 * time on 2018/11/20 0:18
 * description:
 */
@Configuration
@ConditionalOnClass({DruidDataSource.class})
public class DynamicDataSourceConfig {
    @Autowired
    private DynamicDruidProperties druidProperties;

    @Bean
    @ConditionalOnClass({DataSource.class})
    public DataSource createDataSource() {
        DruidDataSource defaultDatasource = new DruidDataSource();
        BeanUtils.copyProperties(druidProperties, defaultDatasource);
        defaultDatasource.setConnectionInitSqls(Collections.singletonList(druidProperties.getConnectionInitSql()));
        // 多数据源
        Map<Object, Object> mutiSources = new HashMap<>();
        mutiSources.put("default", defaultDatasource);
        DynamicDataSourceContextHolder.addDbName("default_datasource");
        if (null != druidProperties.getMuti()) {
            for (Map.Entry<String, DruidMutiProp> druidMutiPropEntry : druidProperties.getMuti().entrySet()) {
                String name = druidMutiPropEntry.getKey();
                DruidMutiProp druidMutiProp = druidMutiPropEntry.getValue();
                DruidDataSource dataSourceTemp = new DruidDataSource();
                BeanUtils.copyProperties(druidProperties, dataSourceTemp);
                dataSourceTemp.setConnectionInitSqls(Collections.singletonList(druidProperties.getConnectionInitSql()));
                DynamicDataSourceContextHolder.addDbName(name);
                dataSourceTemp.setUrl(druidMutiProp.getUrl());
                dataSourceTemp.setUsername(druidMutiProp.getUsername());
                dataSourceTemp.setPassword(druidMutiProp.getPassword());
                mutiSources.put(name, dataSourceTemp);
            }
        }
        DynamicDataSource mutiDatasource = new DynamicDataSource();
        mutiDatasource.setDefaultTargetDataSource(defaultDatasource);
        mutiDatasource.setTargetDataSources(mutiSources);
        return mutiDatasource;
    }
}
