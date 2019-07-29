package com.xfl.boot.common.constant;

import java.util.concurrent.TimeUnit;

/**
 * Created by XFL
 * time on 2019/7/26 1:20
 * description:
 */
public enum Caches {
    ;
    private String cacheName;
    private int ttl;
    private TimeUnit timeUnit;
    private int maxSize;
    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
