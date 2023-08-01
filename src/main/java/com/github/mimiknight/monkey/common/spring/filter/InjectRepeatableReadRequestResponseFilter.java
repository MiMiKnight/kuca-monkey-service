package com.github.mimiknight.monkey.common.spring.filter;

import com.github.mimiknight.monkey.common.spring.servlet.RepeatableReadHttpServletRequest;
import com.github.mimiknight.monkey.common.utils.CommonUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
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
        // TODO 待完善
        if (CommonUtils.isHttpServletRequest(request)) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        RepeatableReadHttpServletRequest repeatableReadHttpServletRequest = new RepeatableReadHttpServletRequest(servletRequest);
        chain.doFilter(repeatableReadHttpServletRequest, response);
    }

}
