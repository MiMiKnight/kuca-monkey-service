package cn.yhm.developer.monkey.common.aspect;


import cn.yhm.developer.monkey.common.constant.AspectRule;
import cn.yhm.developer.monkey.common.constant.ProjectConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 请求跟踪切面
 * <p>
 * spring 5.3.23
 * <p>
 * 异常情况：
 * 1.Around-start => 2.Before => 3.目标方法执行 => 4.AfterThrowing => 5.After
 * 正常情况：
 * 1.Around-start => 2.Before => 3.目标方法执行 => 4.AfterReturning => 5.After=> 6.Around-end
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-02 14:18:33
 */
@Slf4j
@Component
@Aspect
public class RequestTraceAspect implements Ordered {

    /**
     * 切入点
     */
    @Pointcut(AspectRule.Rule001.RULE_PATTERN)
    public void pointcut() {
    }

    /**
     * 环绕通知
     */
    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        // 设置当前请求线程中的跟踪ID
        MDC.put(ProjectConstant.Log.MDC_TRACE_ID_KEY, uuid);
        // 执行被切业务逻辑
        Object proceed = point.proceed();
        // 清除当前请求线程中的跟踪ID
        MDC.clear();
        return proceed;
    }

    @After("pointcut()")
    public void doAfter() throws Throwable {
        // 清除当前请求线程中的跟踪ID
        MDC.clear();
    }


    @Override
    public int getOrder() {
        return AspectRule.Rule001.Order.ORDER_001;
    }
}
