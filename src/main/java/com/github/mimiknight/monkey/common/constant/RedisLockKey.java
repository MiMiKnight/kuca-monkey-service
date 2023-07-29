package com.github.mimiknight.monkey.common.constant;


/**
 * Redis锁键常量类
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-07-29 23:28:39
 */
public interface RedisLockKey {

    /**
     * 文章表记录锁前缀
     */
    String ARTICLE_TABLE_LOCK_KEY_PREFIX = "MonkeyService:Lock:ArticleTable:";

    /**
     * 定时任务锁前缀
     */
    String TASK_LOCK_KEY_PREFIX = "MonkeyService:Lock:Task:";

}
