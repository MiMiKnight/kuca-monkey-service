package cn.yhm.developer.monkey.common.aspect;

import cn.yhm.developer.kuca.common.utils.standard.JsonService;
import cn.yhm.developer.monkey.common.constant.AspectRule;
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
import java.util.Collection;
import java.util.Enumeration;
import java.util.TreeMap;

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

    private JsonService jsonService;

    @Autowired
    public void setJsonService(JsonService jsonService) {
        this.jsonService = jsonService;
    }

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
        traceRequest(httpServletRequest, requestParam);
        // 执行被代理的业务逻辑
        Object proceed = point.proceed();
        // 打印接口正常响应参数日志
        traceResponse(httpServletRequest, httpServletResponse, proceed);
        return proceed;
    }

    /**
     * 跟踪请求
     */
    private void traceRequest(HttpServletRequest servletRequest, Object requestParam) {
        String uri = servletRequest.getRequestURI();
        String method = servletRequest.getMethod();

        TreeMap<String, String> requestHeaderMap = new TreeMap<>();
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = httpServletRequest.getHeader(name);
            requestHeaderMap.put(name, value);
        }

        String headers = jsonService.toJson(requestHeaderMap);
        String requestBodyJson = jsonService.toJson(requestParam);
        log.info("===============================Request Begin===============================");
        log.info("URI          : {}", uri);
        log.info("Method       : {}", method);
        log.info("Headers      : {}", headers);
        log.info("Request Param: {}", requestBodyJson);
        log.info("===============================Request End=================================");
    }

    /**
     * 跟踪响应
     */
    private void traceResponse(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                               Object responseBody) {
        Integer httpStatusCode = servletResponse.getStatus();
        String uri = servletRequest.getRequestURI();
        String method = servletRequest.getMethod();

        TreeMap<String, String> responseHeaderMap = new TreeMap<>();
        Collection<String> headerNames = servletResponse.getHeaderNames();
        for (String name : headerNames) {
            String value = servletResponse.getHeader(name);
            responseHeaderMap.put(name, value);
        }

        String headers = jsonService.toJson(responseHeaderMap);
        String responseBodyJson = jsonService.toJson(responseBody);
        log.info("===============================Response Begin==============================");
        log.info("URI          : {}", uri);
        log.info("Method       : {}", method);
        log.info("Status code  : {}", httpStatusCode);
        log.info("Headers      : {}", headers);
        log.info("Response body: {}", responseBodyJson);
        log.info("===============================Response End================================");
    }

    @Override
    public int getOrder() {
        return AspectRule.Rule003.Order.ORDER_500;
    }
}
