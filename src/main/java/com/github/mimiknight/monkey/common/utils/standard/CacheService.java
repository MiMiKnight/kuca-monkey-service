package com.github.mimiknight.monkey.common.utils.standard;

import java.util.function.Supplier;

/**
 * 缓存服务类接口
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-07-28 07:53:35
 */
public interface CacheService {

    /**
     *
     *
     * @param cacheName 缓存名称
     * @param clazz     clazz
     * @param supplier  供应商
     * @return {@link T}
     */
    <T> T getAndPut(String cacheName, Class<T> clazz, Supplier<T> supplier);
}
