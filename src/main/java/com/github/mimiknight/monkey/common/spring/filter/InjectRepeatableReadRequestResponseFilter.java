package com.github.mimiknight.monkey.common.spring.filter;

import com.github.mimiknight.monkey.common.spring.servlet.RepeatableReadHttpServletRequest;
import com.github.mimiknight.monkey.common.spring.servlet.RepeatableReadHttpServletResponse;
import com.github.mimiknight.monkey.common.utils.CommonUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 注入“可重复读HttpServletRequest”过滤器
 * <p>
 * 将RepeatableReadHttpServletRequest注入到请求参数位置
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-07-31 21:25:38
 */
public class InjectRepeatableReadRequestResponseFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (isTransformToRepeatableReadRequest(request)) {
            HttpServletRequest servletRequest = (HttpServletRequest) request;
            request = new RepeatableReadHttpServletRequest(servletRequest);
        }

        if (isTransformToRepeatableReadResponse(response)) {
            HttpServletResponse servletResponse = (HttpServletResponse) response;
            response = new RepeatableReadHttpServletResponse(servletResponse);
        }

        chain.doFilter(request, response);
    }

    private boolean isTransformToRepeatableReadRequest(ServletRequest request) {
        if (!CommonUtils.isHttpServletRequest(request)) {
            return false;
        }
        return CommonUtils.isJsonContentType((HttpServletRequest) request);
    }

    private boolean isTransformToRepeatableReadResponse(ServletResponse response) {
        return CommonUtils.isHttpServletResponse(response);
    }

}
