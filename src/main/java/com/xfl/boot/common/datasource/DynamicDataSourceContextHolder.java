package com.xfl.boot.common.datasource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XFL
 * time on 2018/11/18 23:42
 * description:
 */
public class DynamicDataSourceContextHolder {
    private static final ThreadLocal<String> handlerThreadLocal = new ThreadLocal<String>();
    /**
     * 管理所有的数据源id
     * 主要是为了判断数据源是否存在
     */
    public static List<String> dataSourceIds = new ArrayList<String>();
    /**
     * 设置数据源
     *
     * @param dataSource
     */
    public static void setDataSource(String dataSource) {
        handlerThreadLocal.set(dataSource);
    }

    /**
     * 获取数据源
     *
     * @return
     */
    public static String getDataSource() {
        return handlerThreadLocal.get();
    }

    /**
     * 移除数据源
     */
    public static void clear() {
        handlerThreadLocal.remove();
    }

    /**
     * 判断指定DataSource当前是否存在
     *
     * @param dataSourceId
     * @return
     */
    public static boolean containsDataSource(String dataSourceId) {
        return dataSourceIds.contains(dataSourceId);
    }

    public static void addDbName(String dataName) {
        dataSourceIds.add(dataName);
    }
}
