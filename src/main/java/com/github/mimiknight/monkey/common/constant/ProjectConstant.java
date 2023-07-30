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
        long DEFAULT_GET_LOCK_WAITE_TIME = 3L;
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

    /**
     * 文章 审核字段 常量
     */
    interface ArticleAudit {
        // 审核中
        Integer AUDITING = 1;
        // 审核通过
        Integer AUDITED = 2;
        // 审核不通过
        Integer NOT_AUDITED = 3;
    }
}
