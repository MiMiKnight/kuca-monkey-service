package com.github.mimiknight.monkey.common.config;

import com.github.mimiknight.monkey.common.spring.filter.ApiLogFilter;
import com.github.mimiknight.monkey.common.spring.filter.InjectRepeatableReadRequestResponseFilter;
import com.github.mimiknight.monkey.common.spring.filter.LogTraceFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * Spring过滤器注册配置类
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-07-31 21:09:19
 */
@Configuration
public class FilterRegistryConfig {

    /**
     * 全路径匹配URL规则
     *
     * @return {@link List}<{@link String}>
     */
    private List<String> allApiPathMatchPattern() {
        return Collections.singletonList("/rest/developer/monkey-service/*");
    }

    /**
     * 获取过滤器名称
     *
     * @param filterClass 过滤器Class对象
     * @return {@link String}
     */
    private String getFilterName(Class<? extends Filter> filterClass) {
        return filterClass.getSimpleName();
    }

    @Bean
    public FilterRegistrationBean<LogTraceFilter> registerLogTraceFilter() {
        FilterRegistrationBean<LogTraceFilter> registrationBean = new FilterRegistrationBean<>();
        // 设置过滤器名称
        registrationBean.setName(getFilterName(LogTraceFilter.class));
        // 注入过滤器
        registrationBean.setFilter(new LogTraceFilter());
        // 过滤器顺序
        registrationBean.setOrder(500);
        // 拦截规则
        registrationBean.setUrlPatterns(allApiPathMatchPattern());
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<InjectRepeatableReadRequestResponseFilter> registerPackRepeatableReadHttpServletRequestFilter() {
        FilterRegistrationBean<InjectRepeatableReadRequestResponseFilter> registrationBean = new FilterRegistrationBean<>();
        // 设置过滤器名称
        registrationBean.setName(getFilterName(InjectRepeatableReadRequestResponseFilter.class));
        // 注入过滤器
        registrationBean.setFilter(new InjectRepeatableReadRequestResponseFilter());
        // 过滤器顺序
        registrationBean.setOrder(501);
        // 拦截规则
        registrationBean.setUrlPatterns(allApiPathMatchPattern());
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<ApiLogFilter> registerApiLogFilter() {
        FilterRegistrationBean<ApiLogFilter> registrationBean = new FilterRegistrationBean<>();
        // 设置过滤器名称
        registrationBean.setName(getFilterName(ApiLogFilter.class));
        // 注入过滤器
        registrationBean.setFilter(new ApiLogFilter());
        // 过滤器顺序
        registrationBean.setOrder(502);
        // 拦截规则
        registrationBean.setUrlPatterns(allApiPathMatchPattern());
        return registrationBean;
    }

}
