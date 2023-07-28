package com.github.mimiknight.monkey;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-09 00:01:17
 */
@Slf4j
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.github.mimiknight.monkey"})
public class Application {

    public static void main(String[] args) {
        // 启动项目
        SpringApplication.run(Application.class, args);
        log.info("The project started successfully.");
    }

}
