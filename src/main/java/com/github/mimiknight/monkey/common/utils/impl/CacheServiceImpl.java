package com.github.mimiknight.monkey.common.utils.impl;

import com.github.mimiknight.kuca.common.utils.standard.RedisService;
import com.github.mimiknight.monkey.common.utils.standard.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 缓存服务实现类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-07-28 07:54:36
 */
@Component
public class CacheServiceImpl implements CacheService {

    private RedisService redisService;

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public <T> T getAndPut(String cacheName, Class<T> returnClass, Supplier<T> code) {
        T result = redisService.get(cacheName, returnClass);
        if (null == result) {
            result = code.get();
            redisService.set(cacheName, result, 24, TimeUnit.HOURS);
        }
        return result;
    }

    @Override
    public <T> T getAndPut(String cacheName, long expireTime, TimeUnit unit, Class<T> returnClass, Supplier<T> code) {
        T result = redisService.get(cacheName, returnClass);
        if (null == result) {
            result = code.get();
            redisService.set(cacheName, result, expireTime, unit);
        }
        return result;
    }
}
