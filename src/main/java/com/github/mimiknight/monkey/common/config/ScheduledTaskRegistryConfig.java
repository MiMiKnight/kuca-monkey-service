package com.github.mimiknight.monkey.common.config;

import com.github.mimiknight.monkey.common.constant.Constant;
import com.github.mimiknight.monkey.common.utils.CommonUtils;
import com.github.mimiknight.monkey.task.BaseCronScheduledTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;

import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 定时任务注册配置类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-07-28 19:34:08
 */
@Slf4j
@EnableScheduling
@Configuration
public class ScheduledTaskRegistryConfig implements SchedulingConfigurer {

    /**
     * 定时任务Map
     */
    private final ConcurrentHashMap<String, ScheduledTask> scheduledTaskMap = new ConcurrentHashMap<>(128);

    /**
     * 本机CPU可用核心数
     */
    private final int processors = Runtime.getRuntime().availableProcessors();
    @Qualifier("CustomThreadPoolExecutor")
    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Autowired
    private ApplicationContext appContext;
    @Autowired
    private Environment environment;

    @Override
    public void configureTasks(@NotNull ScheduledTaskRegistrar taskRegistrar) {
        // 定时任务开关状态
        String status = environment.getProperty(Constant.App.TASK_SWITCH_KEY, Constant.SwitchStatus.OFF);
        if (!CommonUtils.switchStatus(status)) {
            log.info("Task is off,there is no task be registered.");
            return;
        }
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2 * processors);
        // 设置执行定时任务的线程池
        taskRegistrar.setScheduler(scheduledExecutorService);
        // 注册定时任务
        registerCronTask(taskRegistrar);
    }

    private void registerCronTask(ScheduledTaskRegistrar taskRegistrar) {
        Map<String, BaseCronScheduledTask> taskMap = appContext.getBeansOfType(BaseCronScheduledTask.class);
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
