package com.github.mimiknight.monkey.common.constant;

/**
 * 切面规则定义类
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
        String RULE_PATTERN = "@annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping) || @annotation(org.springframework.web.bind.annotation.DeleteMapping) || @annotation(org.springframework.web.bind.annotation.PatchMapping)";

        /**
         * 切面顺序
         */
        interface Order {
            int ORDER_500 = 500;
            int ORDER_501 = 501;
        }
    }

    /**
     * 002切面规则
     */
    interface Rule002 {
        String RULE_PATTERN = "execution(public void com.github.mimiknight.kuca.ecology.core.EcologyRequestHandler.handle(..))";

        interface Order {
            int ORDER_500 = 500;
            int ORDER_501 = 501;
        }
    }

    /**
     * 003切面规则
     */
    interface Rule003 {

        String RULE_PATTERN = "execution(public com.github.mimiknight.kuca.ecology.model.response.SuccessResponse com.github.mimiknight.kuca.ecology.core.HandlerExecutor.execute(com.github.mimiknight.kuca.ecology.model.request.EcologyRequest) throws java.lang.Exception) || execution(public com.github.mimiknight.kuca.ecology.model.response.SuccessResponse com.github.mimiknight.kuca.ecology.core.HandlerExecutor.execute(com.github.mimiknight.kuca.ecology.model.request.EcologyRequest,com.github.mimiknight.kuca.ecology.core.EcologyRequestHandler) throws java.lang.Exception)";

        interface Order {
            int ORDER_500 = 500;
            int ORDER_501 = 501;
        }
    }
}
