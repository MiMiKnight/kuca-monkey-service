package cn.mimiknight.developer.monkey.core.common.constant;

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
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-08-18 22:39:13
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

}
