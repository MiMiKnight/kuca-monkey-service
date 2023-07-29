package com.github.mimiknight.monkey.springtask.scheduled;

import com.github.mimiknight.monkey.springtask.BaseScheduledTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

/**
 * Cron方式触发的定时任务基类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-07-28 19:39:18
 */
@Slf4j
@Component
public abstract class BaseCronScheduledTask extends BaseScheduledTask {


    /**
     * 获取本定时任务的Cron表达式
     *
     * @return {@link String}
     */
    @Override
    public String getCronExpression() {
        // TODO: 此处代码待完善
        String cronExpressionName = this.getTaskName() + "Cron";
        String cronExpression = System.getProperty(cronExpressionName);
        return cronExpression;
    }

    /**
     * 获取为Cron表达式统一设定的时区
     *
     * @return {@link ZoneId}
     */
    @Override
    public ZoneId getCronTimeZone() {
        return ZoneId.of("UTC");
    }
}
