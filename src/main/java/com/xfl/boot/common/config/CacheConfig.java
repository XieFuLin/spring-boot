package com.xfl.boot.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.xfl.boot.common.constant.Caches;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by XFL
 * time on 2019/7/26 1:18
 * description:
 */
@Configuration
public class CacheConfig {
    @Bean
    @Primary
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        // 把各个cache注册到cacheManager中，GuavaCache实现了org.springframework.cache.Cache接口
        List<CaffeineCache> caches = new ArrayList<>();
        for (Caches c : Caches.values()) {
            int ttl =  c.getTtl();
            TimeUnit timeUnit =  c.getTimeUnit();
            caches.add(new CaffeineCache(c.getCacheName(),
                    Caffeine.newBuilder().expireAfterWrite(ttl, timeUnit).maximumSize(c.getMaxSize()).recordStats().build()));
        }
        cacheManager.setCaches(caches);
        return cacheManager;
    }

}
