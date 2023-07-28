package com.github.mimiknight.monkey.common.utils.standard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Supplier;

/**
 * 日志工具类
 *
 * @author victor2015yhm@gmail.com
 * @date 2023-07-07 22:05:38
 */
public interface LogService {

    /**
     * 日志跟踪
     *
     * @param trackedCode 被日志跟踪的代码逻辑
     */
    void logTrace(Runnable trackedCode);

    /**
     * 日志跟踪
     *
     * @param trackedCode 被日志跟踪的代码逻辑
     * @return {@link T}
     */
    <T> T logTrace(Supplier<T> trackedCode);

    /**
     * 跟踪请求
     *
     * @param servletRequest servlet请求
     * @param requestParam   请求参数
     */
    void traceRequest(HttpServletRequest servletRequest, Object requestParam) throws IOException;

    /**
     * 跟踪响应
     *
     * @param servletRequest  servlet请求
     * @param servletResponse servlet响应
     * @param responseBody    身体反应
     */
    void traceResponse(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                       Object responseBody);
}
