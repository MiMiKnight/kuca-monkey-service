package com.github.mimiknight.monkey.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring拦截器注册配置类
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-07-31 21:03:26
 */
@Configuration
public class InterceptorRegistryConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    }
}
