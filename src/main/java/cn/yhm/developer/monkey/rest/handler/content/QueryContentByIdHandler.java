package cn.yhm.developer.monkey.rest.handler.content;

import cn.yhm.developer.kuca.common.utils.standard.RedisService;
import cn.yhm.developer.kuca.ecology.core.EcologyRequestHandler;
import cn.yhm.developer.monkey.common.constant.RedisKey;
import cn.yhm.developer.monkey.model.entity.ContentEntity;
import cn.yhm.developer.monkey.model.request.QueryContentByIdRequest;
import cn.yhm.developer.monkey.model.response.QueryContentByIdResponse;
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
public class QueryContentByIdHandler implements EcologyRequestHandler<QueryContentByIdRequest, QueryContentByIdResponse> {

    private ContentService contentService;

    @Autowired
    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    private RedisService redisService;

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void handle(QueryContentByIdRequest request, QueryContentByIdResponse response) throws Exception {
        // 内容记录id
        String id = request.getId();
        // 内容表缓存键
        String contentTableCacheKey = RedisKey.Cache.CONTENT_TABLE_CACHE + RedisKey.Constant.SEPARATOR + id;

        ContentEntity contentEntity = this.getContentEntity(contentTableCacheKey, id);

        response.setId(contentEntity.getId())
                .setContent(contentEntity.getContent())
                .setVersion(contentEntity.getVersion())
                .setDeleted(contentEntity.getDeleted())
                .setCreatedTime(contentEntity.getCreatedTime())
                .setUpdatedTime(contentEntity.getUpdatedTime());
    }

    /**
     * @param cacheKey 缓存键
     * @param id       内容的主键
     * @return {@link ContentEntity}
     */
    private ContentEntity getContentEntity(String cacheKey, String id) {
        // 从缓存读取
        ContentEntity contentEntity = redisService.get(cacheKey, ContentEntity.class);

        if (null != contentEntity) {
            return contentEntity;
        }
        contentEntity = contentService.getById(id);
        if (null == contentEntity) {
            throw new RuntimeException("Content is not exist.");
        }
        // 设置缓存
        redisService.setIfAbsent(cacheKey, contentEntity, 5L, TimeUnit.MINUTES);
        return contentEntity;
    }
}
