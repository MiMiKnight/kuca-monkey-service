package com.github.mimiknight.monkey.common.spring.filter;

import com.github.mimiknight.monkey.common.utils.CommonUtils;
import com.github.mimiknight.monkey.common.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

        boolean isRequest = CommonUtils.isHttpServletRequest(request);
        if (isRequest) {
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

        boolean isResponse = CommonUtils.isHttpServletResponse(response);
        if (isResponse) {
            // 打印接口出参日志
            LogUtils.traceResponse((HttpServletRequest) request, (HttpServletResponse) response, duration);
        }
    }

}
