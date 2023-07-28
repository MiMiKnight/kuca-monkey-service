package com.github.mimiknight.monkey.common.config;

import com.github.mimiknight.monkey.task.scheduled.BaseScheduledTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;

import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * 定时任务注册配置类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-07-28 19:34:08
 */
@Slf4j
@Configuration
public class ScheduledTaskRegister implements SchedulingConfigurer {

    /**
     * 定时任务Map
     */
    private final ConcurrentHashMap<String, ScheduledTask> scheduledTaskMap = new ConcurrentHashMap<>(128);

    /**
     * 本机CPU可用核心数
     */
    private final int processors = Runtime.getRuntime().availableProcessors();

    private ApplicationContext appContext;

    @Autowired
    public void setAppContext(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public void configureTasks(@NotNull ScheduledTaskRegistrar taskRegistrar) {
        // 设置执行定时任务的线程池
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(5 * processors));
        init(taskRegistrar);
    }

    private void init(ScheduledTaskRegistrar taskRegistrar) {
        Map<String, BaseScheduledTask> taskMap = appContext.getBeansOfType(BaseScheduledTask.class);
        if (MapUtils.isEmpty(taskMap)) {
            log.info("There is no scheduled task need to be registered.");
            return;
        }
        // 循环遍历注册定时任务
        taskMap.forEach((beanName, task) -> {
            String taskName = task.getTaskName();
            String cronExpression = task.getCronExpression();
            ZoneId cronTimeZone = task.getCronTimeZone();
            // 监听配置变化 动态注册
            // cron表达式为空的任务不注册
            if (StringUtils.isBlank(cronExpression)) {
                return;
            }
            ScheduledTask scheduledTask = taskRegistrar.scheduleTriggerTask(new TriggerTask(task, new CronTrigger(cronExpression, cronTimeZone)));
            log.info("Successfully registered scheduled task,task = {}", taskName);
            scheduledTaskMap.put(taskName, scheduledTask);
        });
        log.info("There are {} scheduled task registered successful.", scheduledTaskMap.size());
    }
}
