package com.github.mimiknight.monkey.common.spring.filter;

import com.github.mimiknight.monkey.common.constant.Constant;
import com.github.mimiknight.monkey.common.utils.CommonUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.slf4j.MDC;

import java.io.IOException;

/**
 * 日志跟踪过滤器
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-07-31 21:25:38
 */
public class LogTraceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 设置当前请求线程中的跟踪ID
        MDC.put(Constant.Log.MDC_TRACE_ID_KEY, CommonUtils.uuid());
        // 执行被跟踪的代码逻辑
        chain.doFilter(request, response);
        // 清除当前请求线程中的跟踪ID
        MDC.clear();
    }
}
