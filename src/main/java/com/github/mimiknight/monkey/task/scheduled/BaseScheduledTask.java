package com.github.mimiknight.monkey.task.scheduled;

import com.github.mimiknight.monkey.common.utils.standard.LockService;
import com.github.mimiknight.monkey.common.utils.standard.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务基类
 * <p>
 * 所有自定义定时任务的基类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-07-28 19:39:18
 */
@Slf4j
@Component
public abstract class BaseScheduledTask implements Runnable {

    private LockService lockService;

    @Autowired
    public void setLockService(LockService lockService) {
        this.lockService = lockService;
    }

    private LogService logService;

    @Autowired
    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    @Override
    public void run() {
        logService.logTrace(trackedCode);
    }

    /**
     * 被跟踪代码
     */
    private final Runnable trackedCode = () -> {
        log.info("The scheduled task starts running,task name = {}", this.getTaskName());
        try {
            String lockName = "MonkeyService:Lock:Task:" + this.getTaskName();
            /*
             * 此处加锁且设置获取锁的等待时间为0毫秒，是为了实现所有微服务中的同名定时任务互斥执行；
             * 如果当前存在一个正在执行的定时任务，则本定时人物执行到此处时，不等待获取锁，直接退出执行定时任务；
             */
            lockService.doTryLock(lockName, 0L, TimeUnit.MILLISECONDS, this::doTask);
        } catch (Exception e) {
            // 因为定时任务中的异常没有全局异常处理类统一处理，故在此拦截定时任务中所有抛出的异常，避免污染日志
            log.error("The scheduled task ends abnormally,task name = {},error = {}", this.getTaskName(), e.getMessage());
        }
        log.info("The scheduled task ends normally,task name = {}", this.getTaskName());
    };

    /**
     * 定时任务的抽象方法
     */
    protected abstract void doTask();

    /**
     * 获取定时任务名称
     *
     * @return {@link String}
     */
    public String getTaskName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 获取本定时任务的Cron表达式
     *
     * @return {@link String}
     */
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
    public ZoneId getCronTimeZone() {
        return ZoneId.of("UTC");
    }
}
