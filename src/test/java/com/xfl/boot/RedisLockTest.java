package com.xfl.boot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by XFL
 * time on 2018/1/14 23:44
 * description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisLockTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testLock() {
        new TestLock().run();
        new TestLock().run();
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
