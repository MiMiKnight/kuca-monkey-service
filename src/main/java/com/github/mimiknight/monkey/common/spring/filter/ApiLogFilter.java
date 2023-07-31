package com.github.mimiknight.monkey.common.spring.filter;

import com.github.mimiknight.monkey.common.spring.servlet.RepeatableReadHttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 接口日志打印过滤器
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-07-31 21:25:38
 */
@Slf4j
public class ApiLogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 不是RepeatableReadHttpServletRequest对象，直接放通
        if (!isRepeatableRead(request)) {
            chain.doFilter(request, response);
            return;
        }
        // 如果是RepeatableReadHttpServletRequest对象才打印接口日志
        RepeatableReadHttpServletRequest servletRequest = (RepeatableReadHttpServletRequest) request;
        // 打印接口入参日志
        traceRequest(servletRequest);
        // 放通
        chain.doFilter(servletRequest, response);
        // 打印接口出参日志
        traceResponse(servletRequest, (HttpServletResponse) response);
    }

    /**
     * 是否为RepeatableReadHttpServletRequest对象
     * <p>
     * 是：true
     * <p>
     * 否：false
     *
     * @param request ServletRequest请求
     * @return boolean
     */
    private boolean isRepeatableRead(ServletRequest request) {
        if (null == request) {
            return false;
        }
        if (request instanceof RepeatableReadHttpServletRequest) {
            return true;
        }
        return false;
    }

    /**
     * 打印接口请求入参日志
     *
     * @param request 请求
     */
    private void traceRequest(RepeatableReadHttpServletRequest request) {
        log.info("打印接口请求入参日志");
    }

    /**
     * 打印接口响应出参日志
     *
     * @param request  请求
     * @param response 响应
     */
    private void traceResponse(RepeatableReadHttpServletRequest request, HttpServletResponse response) {
        log.info("打印接口响应出参日志");
    }
}
