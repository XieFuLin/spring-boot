package com.xfl.boot.common.service;

import java.util.List;

/**
 * Created by XFL
 * time on 2017/6/13 22:24
 * description:
 */
public interface IRedisService {
    boolean set(String key, String value);

    String get(String key);

    boolean expire(String key, long expire);

    <T> boolean setList(String key, List<T> list);

    <T> List<T> getList(String key, Class<T> clz);

    long lpush(String key, Object obj);

    long rpush(String key, Object obj);

    String lpop(String key);
}
