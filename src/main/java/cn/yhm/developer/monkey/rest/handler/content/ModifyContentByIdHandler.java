package cn.yhm.developer.monkey.rest.handler.content;

import cn.yhm.developer.kuca.common.utils.standard.RedisLockService;
import cn.yhm.developer.kuca.ecology.core.EcologyRequestHandler;
import cn.yhm.developer.monkey.model.entity.ContentEntity;
import cn.yhm.developer.monkey.model.request.ModifyContentByIdRequest;
import cn.yhm.developer.monkey.model.response.ModifyContentByIdResponse;
import cn.yhm.developer.monkey.service.standard.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 保存内容处理器类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-09 20:32:18
 */
@Component
public class ModifyContentByIdHandler implements EcologyRequestHandler<ModifyContentByIdRequest, ModifyContentByIdResponse> {


    private ContentService contentService;

    @Autowired
    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    private RedisLockService redisLockService;

    @Autowired
    public void setRedisLockService(RedisLockService redisLockService) {
        this.redisLockService = redisLockService;
    }

    @Override
    public void handle(ModifyContentByIdRequest request, ModifyContentByIdResponse response) {
        String id = request.getId();
        String content = request.getContent();
        ContentEntity entity = contentService.getById(id);

        if (null == entity) {
            throw new RuntimeException("Content is not exist");
        }
        // 为业务逻辑加锁
        String lockName = "MonkeyService:Lock:ModifyContentTable:" + id;
        boolean isLock = redisLockService.tryLock(lockName, TimeUnit.SECONDS, 3L);
        // 未获取到锁则抛出异常
        if (!isLock) {
            throw new RuntimeException("Get redis lock failed");
        }
        try {
            entity.setContent(content);
            contentService.updateById(entity);
        } finally {
            // 释放锁
            redisLockService.unlock(lockName);
        }
    }
}
