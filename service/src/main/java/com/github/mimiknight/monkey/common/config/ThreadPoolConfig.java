package com.github.mimiknight.monkey.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 * <p>
 * Io密集型：
 * <p>
 * 线程池核心连接数 = cpu核数*2 + 1
 * <p>
 * CPU密集型：
 * <p>
 * 线程池核心连接数 = cpu核数 + 1
 * <p>
 * ThreadPoolExecutor执行过程
 * <p>
 * 1.当线程数小于核心线程数时，创建线程。
 * <p>
 * 2.当线程数大于等于核心线程数，且任务队列未满时，将任务放入任务队列。
 * <p>
 * 3.当线程数大于等于核心线程数，且任务队列已满。
 * <p>
 * (1)若线程数小于最大线程数，创建线程。
 * <p>
 * (2)若线程数等于最大线程数，抛出异常，拒绝任务。
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-08-02 22:28:08
 */
@Configuration
public class ThreadPoolConfig {

    private TaskExecutionProperties taskExecutionProperties;

    @Autowired
    public void setTaskExecutionProperties(TaskExecutionProperties taskExecutionProperties) {
        this.taskExecutionProperties = taskExecutionProperties;
    }

    @Bean(name = "CustomThreadPoolExecutor")
    public ThreadPoolTaskExecutor threadPoolExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        TaskExecutionProperties.Pool pool = taskExecutionProperties.getPool();
        // 本地处理器数量
        int processors = Runtime.getRuntime().availableProcessors();
        // 线程前缀
        String threadNamePrefix = taskExecutionProperties.getThreadNamePrefix();
        executor.setThreadNamePrefix(threadNamePrefix);
        // 核心线程池数量
        executor.setCorePoolSize(2 * processors);
        // 最大线程数
        executor.setMaxPoolSize(3 * processors);
        // 允许核心线程超时
        executor.setAllowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
        // 非核心线程 闲置时的超时时长
        Duration keepAlive = pool.getKeepAlive();
        executor.setKeepAliveSeconds((int) keepAlive.getSeconds());
        // 设置队列容量
        int queueCapacity = pool.getQueueCapacity();
        executor.setQueueCapacity(queueCapacity);
        // 拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }

}
