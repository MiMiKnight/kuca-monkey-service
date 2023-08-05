package com.github.mimiknight.monkey.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis配置类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-22 07:51:51
 */
@MapperScan("com.github.mimiknight.monkey.mapper")
@Configuration
public class MybatisConfig {


}
