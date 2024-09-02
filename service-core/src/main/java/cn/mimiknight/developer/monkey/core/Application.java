package cn.mimiknight.developer.monkey.core;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动类
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-08-18 22:37:20
 */
@Slf4j
@EnableTransactionManagement // 开启事务管理
@MapperScan(basePackages = {"cn.mimiknight.developer.monkey.core.repository.mapper"}) // MyBatis Mapper包位置扫描
@SpringBootApplication(scanBasePackages = {"cn.mimiknight.developer.monkey"})
public class Application {
    public static void main(String[] args) {
        // 启动项目
        SpringApplication.run(Application.class, args);
        log.info("The project started successfully.");
    }

}
