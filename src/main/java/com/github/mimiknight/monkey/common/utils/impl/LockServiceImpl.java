package com.github.mimiknight.monkey.common.utils.impl;

import com.github.mimiknight.kuca.utils.service.standard.RedisLockService;
import com.github.mimiknight.monkey.common.constant.ProjectConstant;
import com.github.mimiknight.monkey.common.enumeration.ErrorReturn;
import com.github.mimiknight.monkey.common.exception.ServiceException;
import com.github.mimiknight.monkey.common.utils.standard.LockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 锁服务实现类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-07-27 19:42:40
 */
@Slf4j
@Component
public class LockServiceImpl implements LockService {

    private RedisLockService redisLockService;

    @Autowired
    public void setRedisLockService(RedisLockService redisLockService) {
        this.redisLockService = redisLockService;
    }

    /**
     * 执行
     *
     * @param lockName    锁名
     * @param getLockCode 获取锁的代码
     * @param lockedCode  被锁的代码
     * @return {@link T}
     */
    private <T> T execute(String lockName, Predicate<String> getLockCode, Supplier<T> lockedCode) {
        // 如果获取锁失败则抛出异常
        if (!getLockCode.test(lockName)) {
            log.info("Failed to get the lock,lock = {}", lockName);
            throw new ServiceException(ErrorReturn.GET_LOCK_FAILED);
        }
        try {
            return lockedCode.get();
        } finally {
            try {
                redisLockService.unlock(lockName);
            } catch (Exception e) {
                log.error("Failed to release lock manually,lockName = {},error = {}", lockName, e.getMessage());
            }
        }
    }

    /**
     * 执行
     *
     * @param lockName    锁名
     * @param getLockCode 获取锁的代码
     * @param lockedCode  被锁的代码
     */
    private void execute(String lockName, Predicate<String> getLockCode, Runnable lockedCode) {
        // 如果获取锁失败则抛出异常
        if (!getLockCode.test(lockName)) {
            log.info("Failed to get the lock,lock = {}", lockName);
            throw new ServiceException(ErrorReturn.GET_LOCK_FAILED);
        }
        try {
            lockedCode.run();
        } finally {
            try {
                redisLockService.unlock(lockName);
            } catch (Exception e) {
                log.error("Failed to release lock manually,lock name = {},error = {}", lockName, e.getMessage());
            }
        }
    }

    @Override
    public <T> T doTryLock(String lockName, long waitTime, TimeUnit unit, Supplier<T> lockedCode) {
        Predicate<String> getLockCode = key -> redisLockService.tryLock(key, waitTime, unit);
        return execute(lockName, getLockCode, lockedCode);
    }

    @Override
    public <T> T doTryLock(String lockName, Supplier<T> lockedCode) {
        return doTryLock(lockName, ProjectConstant.Lock.GET_LOCK_WAITE_TIME, TimeUnit.SECONDS, lockedCode);
    }

    @Override
    public void doTryLock(String lockName, long waitTime, TimeUnit unit, Runnable lockedCode) {
        Predicate<String> getLockCode = key -> redisLockService.tryLock(key, waitTime, unit);
        execute(lockName, getLockCode, lockedCode);
    }

    @Override
    public void doTryLock(String lockName, Runnable lockedCode) {
        doTryLock(lockName, ProjectConstant.Lock.GET_LOCK_WAITE_TIME, TimeUnit.SECONDS, lockedCode);
    }

}
