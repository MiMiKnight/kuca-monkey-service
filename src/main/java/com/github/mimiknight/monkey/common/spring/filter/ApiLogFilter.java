package com.github.mimiknight.monkey.common.spring.filter;


import com.github.mimiknight.monkey.common.utils.CommonUtils;
import com.github.mimiknight.monkey.common.utils.LogUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import java.time.Duration;
import java.time.Instant;

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

        if (CommonUtils.isHttpServletRequest(request)) {
            // 打印接口入参日志
            LogUtils.traceRequest((HttpServletRequest) request);
        }
        // 开始时间
        Instant start = Instant.now();
        // 执行过滤器责任链
        chain.doFilter(request, response);
        // 结束时间
        Instant end = Instant.now();
        // 接口耗时
        Duration duration = Duration.between(start, end);

        if (CommonUtils.isHttpServletResponse(response)) {
            // 打印接口出参日志
            LogUtils.traceResponse((HttpServletRequest) request, (HttpServletResponse) response, duration);
        }
    }

}
