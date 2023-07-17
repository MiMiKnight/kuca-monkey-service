package cn.yhm.developer.monkey;

import cn.yhm.developer.monkey.common.utils.standard.EncryptDecryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-09 00:01:17
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"cn.yhm.developer.monkey"})
public class Application {

    public static void main(String[] args) {
        // 启动项目
        SpringApplication.run(Application.class, args);
        log.info("The project started successfully.");

    }

}
