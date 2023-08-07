package com.github.mimiknight.monkey.common.utils.standard;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 缓存服务类接口
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-07-28 07:53:35
 */
public interface CacheService {

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
    <T> T getAndPut(String cacheName, Class<T> returnClass, Supplier<T> code);

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
    <T> T getAndPut(String cacheName, long expireTime, TimeUnit unit, Class<T> returnClass, Supplier<T> code);
}
