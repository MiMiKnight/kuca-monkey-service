package com.github.mimiknight.monkey.common.utils.impl;

import com.github.mimiknight.kuca.utils.service.standard.RedisService;
import com.github.mimiknight.monkey.common.constant.Constant;
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

    @Autowired
    private RedisService redisService;

    /**
     * 获取或设置缓存
     *
     * @param cacheName   缓存名称
     * @param expireTime  缓存到期时间
     * @param unit        缓存到期时间单位
     * @param returnClass 返回值Class类型
     * @param code        执行代码
     * @return {@link T}
     */
    private <T> T doGetAndPut(String cacheName, long expireTime, TimeUnit unit, Class<T> returnClass, Supplier<T> code) {
        T result = redisService.get(cacheName, returnClass);
        if (null == result) {
            result = code.get();
            redisService.set(cacheName, result, expireTime, unit);
        }
        return result;
    }

    /**
     * 获取或设置缓存
     * <p>
     * 默认缓存过期时间：24小时
     *
     * @param cacheName   缓存名称
     * @param returnClass 返回值Class类型
     * @param code        执行代码
     * @return {@link T}
     */
    @Override
    public <T> T getAndPut(String cacheName, Class<T> returnClass, Supplier<T> code) {
        return getAndPut(cacheName, Constant.Redis.DEFAULT_CACHE_EXPIRE_TIME, TimeUnit.HOURS, returnClass, code);
    }

    /**
     * 获取或设置缓存
     *
     * @param cacheName   缓存名称
     * @param expireTime  缓存到期时间
     * @param unit        缓存到期时间单位
     * @param returnClass 返回值Class类型
     * @param code        执行代码
     * @return {@link T}
     */
    @Override
    public <T> T getAndPut(String cacheName, long expireTime, TimeUnit unit, Class<T> returnClass, Supplier<T> code) {
        return doGetAndPut(cacheName, expireTime, unit, returnClass, code);
    }
}
