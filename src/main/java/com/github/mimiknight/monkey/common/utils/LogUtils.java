package com.github.mimiknight.monkey.common.utils;

import com.github.mimiknight.monkey.common.constant.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.time.Duration;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * 日志工具类
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-08-01 22:26:19
 */
@Slf4j
public class LogUtils {

    private LogUtils() {
    }

    /**
     * 日志跟踪
     *
     * @param trackedCode 跟踪代码
     */
    public static void logTrace(Runnable trackedCode) {
        // 设置当前请求线程中的跟踪ID
        MDC.put(Constant.Log.MDC_TRACE_ID_KEY, CommonUtils.uuid());
        // 执行被跟踪的代码逻辑
        trackedCode.run();
        // 清除当前请求线程中的跟踪ID
        MDC.clear();
    }


    /**
     * 日志跟踪
     *
     * @param trackedCode 跟踪代码
     * @return {@link T}
     */
    public static <T> T logTrace(Supplier<T> trackedCode) {
        // 设置当前请求线程中的跟踪ID
        MDC.put(Constant.Log.MDC_TRACE_ID_KEY, CommonUtils.uuid());
        // 执行被跟踪的代码逻辑
        T result = trackedCode.get();
        // 清除当前请求线程中的跟踪ID
        MDC.clear();
        return result;
    }


    /**
     * 跟踪请求
     *
     * @param request 请求
     */
    public static void traceRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        TreeMap<String, String> headerMap = new TreeMap<>();
        Object requestQuery = CommonUtils.requestQuery(request);
        Object requestBody = CommonUtils.requestBody(request);
        ApiLogFormat.formatRequest(uri, method, headerMap, requestQuery, requestBody);
    }

    /**
     * 跟踪响应
     *
     * @param request  请求
     * @param response 响应
     * @param duration 接口响应耗时
     */
    public static void traceResponse(HttpServletRequest request, HttpServletResponse response, Duration duration) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        int statusCode = response.getStatus();
        TreeMap<String, String> headerMap = new TreeMap<>();
        Object responseBody = CommonUtils.responseBody(response);
        ApiLogFormat.formatResponse(uri, method, headerMap, statusCode, duration, responseBody);
    }


}
