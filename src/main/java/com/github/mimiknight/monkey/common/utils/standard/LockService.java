package com.github.mimiknight.monkey.common.utils.standard;


import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 锁服务类接口
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-07-27 19:39:10
 */
public interface LockService {

    /**
     * 给代码块上锁
     * <p>
     * doTryLock() 在超过等待时间过后如果还未获取到锁，则获取锁失败（false）
     *
     * @param lockName   锁名
     * @param waitTime   锁等待时间
     * @param unit       锁时间单位
     * @param lockedCode 被上锁的代码
     * @return {@link T}
     */
    <T> T doTryLock(String lockName, long waitTime, TimeUnit unit, Supplier<T> lockedCode);

    /**
     * 给代码块上锁
     * <p>
     * doLock() 如果没有获取到锁则一直等待，直到锁被释放
     *
     * @param lockName   锁名
     * @param lockedCode 供应商
     * @return {@link T}
     */
    <T> T doLock(String lockName, Supplier<T> lockedCode);

}
