package com.github.mimiknight.monkey.common.constant;

/**
 * 项目常量
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-12 10:39:05
 */
public interface ProjectConstant {

    /**
     * 项目相关常量
     */
    interface App {

        /**
         * 项目名称
         */
        String NAME = "MonkeyService";
    }


    /**
     * 日志相关常量
     */
    interface Log {

        /**
         * MDC跟踪Key
         */
        String MDC_TRACE_ID_KEY = "TRACE_ID";

    }

    /**
     * 锁 相关常量
     */
    interface Lock {
        /**
         * 项目默认获取锁的等待时间
         * <p>
         * 时间单位：秒
         */
        long GET_LOCK_WAITE_TIME = 3L;
    }

    /**
     * 缓存 相关常量
     */
    interface Cache {
        /**
         * 项目默认缓存过期时间
         * <p>
         * 时间单位：小时
         */
        long EXPIRE_TIME = 24L;


        /**
         * 分隔符
         */
        String SEPARATOR = ":";
    }
}
