package com.github.mimiknight.monkey.task.scheduled;

import com.github.mimiknight.monkey.task.BaseCronScheduledTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Hello定时任务
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-07-28 20:57:27
 */
@Slf4j
@Component
public class HelloScheduledTask extends BaseCronScheduledTask {
    @Override
    protected void doTask() {
        log.info("Hello Scheduled Task...01");
    }

    /**
     * 得到cron表达式
     *
     * @return {@link String}
     */
    @Override
    public String getCronExpression() {
        return "0/20 * * * * ? ";
    }
}
