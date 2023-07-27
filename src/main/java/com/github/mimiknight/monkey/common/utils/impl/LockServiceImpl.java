package com.github.mimiknight.monkey.common.utils.impl;

import cn.yhm.developer.kuca.common.utils.standard.RedisLockService;
import com.github.mimiknight.monkey.common.enumeration.ErrorReturn;
import com.github.mimiknight.monkey.common.exception.ServiceException;
import com.github.mimiknight.monkey.common.utils.standard.LockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class LockServiceImpl implements LockService {

    private RedisLockService redisLockService;

    @Autowired
    public void setRedisLockService(RedisLockService redisLockService) {
        this.redisLockService = redisLockService;
    }

    /**
     * 执行
     *
     * @param lockName   锁名字
     * @param getLock    获取锁的代码
     * @param lockedCode 被锁的代码
     * @return {@link T}
     */
    private <T> T execute(String lockName, Predicate<String> getLock, Supplier<T> lockedCode) {
        // 如果获取锁失败则抛出异常
        if (!getLock.test(lockName)) {
            log.info("Failed to get the lock,lock = {}", lockName);
            throw new ServiceException(ErrorReturn.GET_LOCK_FAILED, "Failed to get the lock.");
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

    @Override
    public <T> T doTryLock(String lockName, long waitTime, TimeUnit unit, Supplier<T> lockedCode) {
        Predicate<String> getLock = key -> redisLockService.tryLock(key, waitTime, unit);
        return execute(lockName, getLock, lockedCode);
    }

    @Override
    public <T> T doLock(String lockName, Supplier<T> lockedCode) {
        Predicate<String> getLock = key -> redisLockService.lock(key);
        return execute(lockName, getLock, lockedCode);
    }
}
