package com.github.mimiknight.monkey;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author victor2015yhm@gmail.com
 * @date 2023-08-06 00:54:19
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"com.github.mimiknight.monkey"})
public class Application {

    public static void main(String[] args) {
        // 启动项目
        SpringApplication.run(Application.class, args);
        log.info("The project started successfully.");
    }

}
