package com.github.mimiknight.monkey.common.constant;

/**
 * Redis常量
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-02 15:25:21
 */
public interface RedisKey {

    interface Constant {

        String TOP_PREFIX = ProjectConstant.App.NAME;

        String SEPARATOR = ":";

        String LOCK_PREFIX = "Lock";

        String CACHE_PREFIX = "Cache";
    }


    /**
     * 锁键
     */
    interface Lock {

        /**
         * 内容表锁
         */
        String CONTENT_TABLE_LOCK = buildLock("ContentTable");

    }

    /**
     * 缓存键
     */
    interface Cache {
        String CONTENT_TABLE_CACHE =    buildCache("ContentTable");
    }


    /**
     * 构建
     *
     * @param topPrefix 键顶级前缀
     * @param type      类型
     * @param key       key
     * @param separator 分隔符
     * @return {@link String}
     */
    static String build(String topPrefix, String type, String key, String separator) {
        return topPrefix + separator + type + separator + key;
    }

    /**
     * 构造锁
     *
     * @param key key
     * @return {@link String}
     */
    static String buildLock(String key) {
        return build(Constant.TOP_PREFIX, Constant.LOCK_PREFIX, key, Constant.SEPARATOR);
    }

    /**
     * 构造锁
     *
     * @param key key
     * @return {@link String}
     */
    static String buildCache(String key) {
        return build(Constant.TOP_PREFIX, Constant.CACHE_PREFIX, key, Constant.SEPARATOR);
    }

}
