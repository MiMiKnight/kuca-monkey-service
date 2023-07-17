package cn.yhm.developer.monkey.common.utils.standard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 日志工具类
 *
 * @author victor2015yhm@gmail.com
 * @date 2023-07-07 22:05:38
 */
public interface LogUtil {

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
