package com.xfl.boot.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 */
@ConfigurationProperties(prefix = "httpclient.config")
public class RestTemplateProperties {
    private int connectTimeout = 3000;// 连接超时

    private int readTimeout = 5000;// 数据读取超时时间

    private int connectionRequestTimeout = 2000;// 从连接池获取连接的超时时间,如果连接池里连接都被用了，且超过这个connectionRequestTimeout，会抛出超时异常

    private int retryTime;// 重试次数

    private int connMaxTotal = 20;// 连接池最大连接数

    private int maxPerRoute = 20;// 同路由并发数

    private int timeToLive = 60;// 连接存活时间，单位为s

    /**
     * @return the connectTimeout
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * @param connectTimeout the connectTimeout to set
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * @return the readTimeout
     */
    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * @param readTimeout the readTimeout to set
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * @return the connectionRequestTimeout
     */
    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    /**
     * @param connectionRequestTimeout the connectionRequestTimeout to set
     */
    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    /**
     * @return the retryTime
     */
    public int getRetryTime() {
        return retryTime;
    }

    /**
     * @param retryTime the retryTime to set
     */
    public void setRetryTime(int retryTime) {
        this.retryTime = retryTime;
    }

    /**
     * @return the connMaxTotal
     */
    public int getConnMaxTotal() {
        return connMaxTotal;
    }

    /**
     * @param connMaxTotal the connMaxTotal to set
     */
    public void setConnMaxTotal(int connMaxTotal) {
        this.connMaxTotal = connMaxTotal;
    }

    /**
     * @return the maxPerRoute
     */
    public int getMaxPerRoute() {
        return maxPerRoute;
    }

    /**
     * @param maxPerRoute the maxPerRoute to set
     */
    public void setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    /**
     * @return the timeToLive
     */
    public int getTimeToLive() {
        return timeToLive;
    }

    /**
     * @param timeToLive the timeToLive to set
     */
    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

}
