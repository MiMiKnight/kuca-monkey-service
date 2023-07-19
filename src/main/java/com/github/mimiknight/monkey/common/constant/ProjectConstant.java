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
}
