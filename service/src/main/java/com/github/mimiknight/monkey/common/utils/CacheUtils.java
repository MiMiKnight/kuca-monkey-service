package com.github.mimiknight.monkey.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 缓存工具类
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-08-02 06:19:58
 */
@Slf4j
public class CacheUtils {

    private CacheUtils() {
    }

    /**
     * 获取并赋值
     * <p>
     * 如果缓存存在则直接从缓存中取值并返回,
     * 如果缓存不存在则从传入的code处取值，并设置缓存;
     * <p>
     * 默认缓存过期时间：24小时
     *
     * @param cacheName   缓存名称
     * @param returnClass 返回值对象Class类型
     * @param code        代码块
     * @return {@link T}
     */
    public static <T> T getAndPut(String cacheName, Class<T> returnClass, Supplier<T> code) {
        return null;
    }

    /**
     * get和put
     * 获取并赋值
     * <p>
     * 如果缓存存在则直接从缓存中取值并返回,
     * 如果缓存不存在则从传入的code处取值，并设置缓存;
     * <p>
     *
     * @param cacheName   缓存名称
     * @param expireTime  缓存过期时间
     * @param unit        缓存过期时间单位
     * @param returnClass 返回值对象Class类型
     * @param code        代码块
     * @return {@link T}
     */
    public static <T> T getAndPut(String cacheName, long expireTime, TimeUnit unit, Class<T> returnClass, Supplier<T> code) {
        return null;
    }

}
