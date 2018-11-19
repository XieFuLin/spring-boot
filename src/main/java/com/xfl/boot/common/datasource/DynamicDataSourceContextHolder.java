package com.xfl.boot.common.datasource;

/**
 * Created by XFL
 * time on 2018/11/18 23:42
 * description:
 */
public class DynamicDataSourceContextHolder {
    private static final ThreadLocal<String> handlerThredLocal = new ThreadLocal<String>();

    /**
     * 设置数据源
     *
     * @param dataSource
     */
    public static void setDataSource(String dataSource) {
        handlerThredLocal.set(dataSource);
    }

    /**
     * 获取数据源
     *
     * @return
     */
    public static String getDataSource() {
        return handlerThredLocal.get();
    }

    /**
     * 移除数据源
     */
    public static void clear() {
        handlerThredLocal.remove();
    }
}
