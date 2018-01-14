package com.xfl.boot;

import com.xfl.boot.common.utils.concurrent.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by XFL
 * time on 2018/1/15 0:16
 * description:
 */
public class TestLock implements Runnable {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void run() {
        String key = "122456";
        RedisLock lock = new RedisLock(stringRedisTemplate, key.toString(),
                1000, 2000);
        try {
            if (lock.lock()) {
                System.out.println("get lock success");
                Thread.sleep(1000);
            } else {
                System.out.println("can not get lock");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
