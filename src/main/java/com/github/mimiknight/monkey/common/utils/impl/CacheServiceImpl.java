package com.github.mimiknight.monkey.common.utils.impl;

import com.github.mimiknight.kuca.common.utils.standard.RedisService;
import com.github.mimiknight.monkey.common.utils.standard.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

/**
 * 缓存服务实现类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-07-28 07:54:36
 */
@Service
public class CacheServiceImpl implements CacheService {

    private RedisService redisService;

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public <T> T getAndPut(String cacheName, Class<T> clazz, Supplier<T> supplier) {
        T result = redisService.get(cacheName, clazz);
        if (null == result) {
            result = supplier.get();
            redisService.set(cacheName, result);
        }
        return result;
    }
}
