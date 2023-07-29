package com.github.mimiknight.monkey.task.scheduled;

import com.github.mimiknight.monkey.task.BaseCronScheduledTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 注销用户定时任务
 *
 * @author MiMiKnight <victor2015yhm@gmail.com>
 * @since 2023-07-30 00:24:11
 */
@Slf4j
@Component
public class LogoffUserScheduledTask extends BaseCronScheduledTask {
    @Override
    protected void doTask() {
        log.info("Logoff User....");
    }

    /**
     * 获取本定时任务的Cron表达式
     * <p>
     * 0 0 2 1 * ?   表示在每月1日的凌晨2点执行任务
     *
     * @return {@link String}
     */
    @Override
    public String getCronExpression() {
        return "0 0 2 1 * ?";
    }
}
