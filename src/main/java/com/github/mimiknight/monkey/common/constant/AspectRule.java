package com.github.mimiknight.monkey.common.constant;

/**
 * 切面规则定义类
 *
 * <p>
 * spring 5.3.23
 * <p>
 * 异常情况：
 * 1.Around-start => 2.Before => 3.目标方法执行 => 4.AfterThrowing => 5.After
 * 正常情况：
 * 1.Around-start => 2.Before => 3.目标方法执行 => 4.AfterReturning => 5.After=> 6.Around-end
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-02 15:24:30
 */
public interface AspectRule {

    /**
     * 001切面规则
     */
    interface Rule001 {

        /**
         * 切面规则
         */
        String RULE_PATTERN = "";

        /**
         * 切面顺序
         */
        interface Order {
            int ORDER_500 = 500;
            int ORDER_501 = 501;
        }
    }

}
