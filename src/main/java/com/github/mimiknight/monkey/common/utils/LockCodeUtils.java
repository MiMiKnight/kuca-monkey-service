package com.github.mimiknight.monkey.common.utils;

import com.github.mimiknight.kuca.utils.service.standard.RedisLockService;
import com.github.mimiknight.monkey.common.constant.Constant;
import com.github.mimiknight.monkey.common.enumeration.ErrorReturn;
import com.github.mimiknight.monkey.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 代码锁工具类
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-08-02 06:19:32
 */
@Slf4j
@Component
public class LockCodeUtils {

    @Autowired
    private RedisLockService redisLockService;
    private static LockCodeUtils instance;

    private LockCodeUtils() {
    }

    @PostConstruct
    public void init() {
        instance = this;
        instance.redisLockService = this.redisLockService;
    }

    /**
     * 执行
     *
     * @param lockName    锁名
     * @param getLockCode 获取锁的代码
     * @param lockedCode  被锁的代码
     * @return {@link T}
     */
    private static <T> T execute(String lockName, Predicate<String> getLockCode, Supplier<T> lockedCode) {
        // 如果获取锁失败则抛出异常
        if (!getLockCode.test(lockName)) {
            log.info("Failed to get the lock,lock = {}", lockName);
            throw new ServiceException(ErrorReturn.GET_LOCK_FAILED);
        }
        try {
            return lockedCode.get();
        } finally {
            instance.redisLockService.unlock(lockName);
        }
    }

    /**
     * 执行
     *
     * @param lockName    锁名
     * @param getLockCode 获取锁的代码
     * @param lockedCode  被锁的代码
     */
    private static void execute(String lockName, Predicate<String> getLockCode, Runnable lockedCode) {
        // 如果获取锁失败则抛出异常
        if (!getLockCode.test(lockName)) {
            log.info("Failed to get the lock,lock = {}", lockName);
            throw new ServiceException(ErrorReturn.GET_LOCK_FAILED);
        }
        try {
            lockedCode.run();
        } finally {
            instance.redisLockService.unlock(lockName);
        }
    }

    public static <T> T doTryLock(String lockName, long waitTime, TimeUnit unit, Supplier<T> lockedCode) {
        Predicate<String> getLockCode = key -> instance.redisLockService.tryLock(key, waitTime, unit);
        return execute(lockName, getLockCode, lockedCode);
    }

    public static <T> T doTryLock(String lockName, Supplier<T> lockedCode) {
        return doTryLock(lockName, Constant.Redis.DEFAULT_GET_LOCK_WAITE_TIME, TimeUnit.SECONDS, lockedCode);
    }

    public static void doTryLock(String lockName, long waitTime, TimeUnit unit, Runnable lockedCode) {
        Predicate<String> getLockCode = key -> instance.redisLockService.tryLock(key, waitTime, unit);
        execute(lockName, getLockCode, lockedCode);
    }

    public static void doTryLock(String lockName, Runnable lockedCode) {
        doTryLock(lockName, Constant.Redis.DEFAULT_GET_LOCK_WAITE_TIME, TimeUnit.SECONDS, lockedCode);
    }

}
