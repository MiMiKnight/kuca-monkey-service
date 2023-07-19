package com.github.mimiknight.monkey.common.constant;

/**
 * Handler拦截器顺序常量
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-02 15:24:48
 */
public interface HandlerInterceptorOrder {

    /**
     * 前置拦截
     */
    interface Before {

        /**
         * 审核内容接口
         */
        interface AuditContent {
            int ORDER_001 = 500;
            int ORDER_002 = 501;
            int ORDER_003 = 502;
        }

    }

    /**
     * 后置拦截
     */
    interface AfterReturn {

    }
}
