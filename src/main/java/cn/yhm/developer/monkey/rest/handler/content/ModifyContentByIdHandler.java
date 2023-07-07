package cn.yhm.developer.monkey.rest.handler.content;

import cn.yhm.developer.kuca.common.utils.standard.RedisLockService;
import cn.yhm.developer.kuca.ecology.core.EcologyRequestHandler;
import cn.yhm.developer.monkey.common.enumeration.ErrorReturn;
import cn.yhm.developer.monkey.common.exception.ServiceException;
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
            String tip = String.format("id = %s ,content is not exist.", id);
            throw new ServiceException(ErrorReturn.CONTENT_DO_NOT_EXIST, tip);
        }
        // 为业务逻辑加锁
        String lockName = "MonkeyService:Lock:ModifyContentTable:" + id;
        boolean isLock = redisLockService.tryLock(lockName, 3L, TimeUnit.SECONDS);
        // 未获取到锁则抛出异常
        if (!isLock) {
            String tip = String.format("Get redis lock failed,lock name = %s.", lockName);
            throw new ServiceException(ErrorReturn.GET_MODIFY_CONTENT_TABLE_LOCK_FAILED, tip);
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
