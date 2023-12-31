package com.github.mimiknight.monkey.common.aspect;

import com.github.mimiknight.monkey.common.constant.AspectRule;
import com.github.mimiknight.monkey.common.utils.standard.LogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求入参日志切面
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-06-29 21:42:03
 */
@Slf4j
@Component
@Aspect
public class ApiLogAspect implements Ordered {

    private HttpServletRequest httpServletRequest;

    @Autowired
    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    private HttpServletResponse httpServletResponse;

    @Autowired
    public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    private LogService logService;

    @Autowired
    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    /**
     * 切入点
     */
    @Pointcut(value = AspectRule.Rule003.RULE_PATTERN)
    public void pointcut() {
    }

    /**
     * 环绕通知
     */
    @Around(value = "pointcut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        Object requestParam = point.getArgs()[0];
        // 打印接口请求参数日志
        logService.traceRequest(httpServletRequest, requestParam);
        // 执行被代理的业务逻辑
        Object proceed = point.proceed();
        // 打印接口正常响应参数日志
        logService.traceResponse(httpServletRequest, httpServletResponse, proceed);
        return proceed;
    }

    @Override
    public int getOrder() {
        return AspectRule.Rule003.Order.ORDER_500;
    }
}
