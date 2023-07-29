package com.github.mimiknight.monkey.common.utils.impl;

import com.github.mimiknight.kuca.utils.service.standard.JsonService;
import com.github.mimiknight.monkey.common.constant.ProjectConstant;
import com.github.mimiknight.monkey.common.utils.standard.LogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * 日志工具类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-07-07 21:50:40
 */
@Slf4j
@Component
public class LogServiceImpl implements LogService {

    private JsonService jsonService;

    @Autowired
    public void setJsonService(JsonService jsonService) {
        this.jsonService = jsonService;
    }

    /**
     * 日志跟踪方法
     *
     * @param trackedCode 被跟踪的代码逻辑
     */
    @Override
    public void logTrace(Runnable trackedCode) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        // 设置当前请求线程中的跟踪ID
        MDC.put(ProjectConstant.Log.MDC_TRACE_ID_KEY, uuid);
        // 执行被跟踪的代码逻辑
        trackedCode.run();
        // 清除当前请求线程中的跟踪ID
        MDC.clear();
    }

    @Override
    public <T> T logTrace(Supplier<T> trackedCode) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        // 设置当前请求线程中的跟踪ID
        MDC.put(ProjectConstant.Log.MDC_TRACE_ID_KEY, uuid);
        // 执行被跟踪的代码逻辑
        T result = trackedCode.get();
        // 清除当前请求线程中的跟踪ID
        MDC.clear();
        return result;
    }

    @Override
    public void traceRequest(HttpServletRequest servletRequest, Object requestParam) throws IOException {
        String uri = servletRequest.getRequestURI();
        String method = servletRequest.getMethod();

        TreeMap<String, String> requestHeaderMap = new TreeMap<>();
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = servletRequest.getHeader(name);
            requestHeaderMap.put(name, value);
        }

        String headers = jsonService.toJson(requestHeaderMap);
        String requestBodyJson = jsonService.toJson(requestParam);
        String requestParamJson = "";
        log.info("===============================Request Begin===============================");
        log.info("URI          : {}", uri);
        log.info("Method       : {}", method);
        log.info("Headers      : {}", headers);
        log.info("Request Query: {}", requestParamJson);
        log.info("Request Body : {}", requestBodyJson);
        log.info("===============================Request End=================================");
    }

    @Override
    public void traceResponse(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
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

    /**
     * 判断本次请求的数据类型是否为json
     *
     * @param contentType 内容类型
     * @return boolean
     */
    private boolean contentTypeIsJson(String contentType) {
        if (StringUtils.isBlank(contentType)) {
            return false;
        }
        return contentType.contains(MediaType.APPLICATION_JSON_VALUE);
    }
}
