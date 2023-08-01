package com.github.mimiknight.monkey.common.spring.filter;

import com.github.mimiknight.monkey.common.constant.ProjectConstant;
import com.github.mimiknight.monkey.common.utils.CommonUtils;
import org.slf4j.MDC;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.UUID;

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
        MDC.put(ProjectConstant.Log.MDC_TRACE_ID_KEY, CommonUtils.uuid());
        // 执行被跟踪的代码逻辑
        chain.doFilter(request, response);
        // 清除当前请求线程中的跟踪ID
        MDC.clear();
    }
}
