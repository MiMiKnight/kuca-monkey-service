package com.github.mimiknight.monkey.common.utils.impl;

import com.github.mimiknight.kuca.utils.service.standard.RedisService;
import com.github.mimiknight.monkey.common.constant.ProjectConstant;
import com.github.mimiknight.monkey.common.utils.standard.CacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
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

    /**
     * 做get和put
     *
     * @param cacheName   缓存名称
     * @param returnClass 返回类
     * @param code        代码
     * @param function    函数
     * @return {@link T}
     */
    private <T> T doGetAndPut(String cacheName, Class<T> returnClass, Supplier<T> code, Function<Supplier<T>, T> function) {
        T result = redisService.get(cacheName, returnClass);
        if (null == result) {
            result = function.apply(code);
        }
        return result;
    }

    @Override
    public <T> T getAndPut(String cacheName, Class<T> returnClass, Supplier<T> code) {
        return doGetAndPut(cacheName, returnClass, code, t -> {
            T result = code.get();
            redisService.set(cacheName, result, ProjectConstant.Cache.EXPIRE_TIME, TimeUnit.HOURS);
            return result;
        });
    }

    @Override
    public <T> T getAndPut(String cacheName, long expireTime, TimeUnit unit, Class<T> returnClass, Supplier<T> code) {
        return doGetAndPut(cacheName, returnClass, code, t -> {
            T result = code.get();
            redisService.set(cacheName, result, expireTime, unit);
            return result;
        });
    }
}
