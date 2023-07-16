package cn.yhm.developer.monkey.rest.handler.article;

import cn.yhm.developer.kuca.common.utils.standard.RedisService;
import cn.yhm.developer.kuca.ecology.core.EcologyRequestHandler;
import cn.yhm.developer.monkey.common.constant.RedisKey;
import cn.yhm.developer.monkey.model.entity.ArticleEntity;
import cn.yhm.developer.monkey.model.request.QueryArticleByIdRequest;
import cn.yhm.developer.monkey.model.response.QueryArticleByIdResponse;
import cn.yhm.developer.monkey.service.standard.ArticleService;
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
public class QueryArticleByIdHandler implements EcologyRequestHandler<QueryArticleByIdRequest, QueryArticleByIdResponse> {

    private ArticleService articleService;

    @Autowired
    public void setContentService(ArticleService contentService) {
        this.articleService = contentService;
    }

    private RedisService redisService;

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void handle(QueryArticleByIdRequest request, QueryArticleByIdResponse response) throws Exception {
        // 内容记录id
        String id = request.getId();
        // 内容表缓存键
        String contentTableCacheKey = RedisKey.Cache.CONTENT_TABLE_CACHE + RedisKey.Constant.SEPARATOR + id;

        ArticleEntity articleEntity = this.getArticleEntity(contentTableCacheKey, id);

        response.setId(articleEntity.getId())
                .setTitle(articleEntity.getTitle())
                .setArticle(articleEntity.getArticle())
                .setVersion(articleEntity.getVersion())
                .setDeleted(articleEntity.getDeleted())
                .setCreatedTime(articleEntity.getCreatedTime())
                .setUpdatedTime(articleEntity.getUpdatedTime());
    }

    /**
     * @param cacheKey 缓存键
     * @param id       内容的主键
     * @return {@link ArticleEntity}
     */
    private ArticleEntity getArticleEntity(String cacheKey, String id) {
        // 从缓存读取
        ArticleEntity contentEntity = redisService.get(cacheKey, ArticleEntity.class);

        if (null != contentEntity) {
            return contentEntity;
        }
        contentEntity = articleService.getById(id);
        if (null == contentEntity) {
            throw new RuntimeException("Content is not exist.");
        }
        // 设置缓存
        redisService.setIfAbsent(cacheKey, contentEntity, 5L, TimeUnit.MINUTES);
        return contentEntity;
    }
}
