package com.github.mimiknight.monkey.rest.handler.article;

import com.github.mimiknight.kuca.ecology.core.EcologyRequestHandler;
import com.github.mimiknight.monkey.common.constant.RedisCacheKey;
import com.github.mimiknight.monkey.common.enumeration.ErrorReturn;
import com.github.mimiknight.monkey.common.exception.ServiceException;
import com.github.mimiknight.monkey.common.utils.standard.CacheService;
import com.github.mimiknight.monkey.model.entity.ArticleEntity;
import com.github.mimiknight.monkey.model.request.QueryArticleByIdRequest;
import com.github.mimiknight.monkey.model.response.QueryArticleByIdResponse;
import com.github.mimiknight.monkey.service.standard.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private CacheService cacheService;

    @Autowired
    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public void handle(QueryArticleByIdRequest request, QueryArticleByIdResponse response) throws Exception {
        // 内容记录id
        String id = request.getId();
        // 文章表记录缓存键
        String articleTableRecordCacheKey = RedisCacheKey.ARTICLE_TABLE_CACHE_KEY_PREFIX + id;
        // 缓存存在结果从缓存中读取，没有则从数据查询结果
        ArticleEntity articleEntity = cacheService.getAndPut(articleTableRecordCacheKey, ArticleEntity.class, () -> {
            ArticleEntity entity = articleService.getById(id);
            if (null == entity) {
                throw new ServiceException(ErrorReturn.ARTICLE_DO_NOT_EXIST);
            }
            return entity;
        });

        response.setId(articleEntity.getId())
                .setTitle(articleEntity.getTitle())
                .setArticle(articleEntity.getArticle())
                .setVersion(articleEntity.getVersion())
                .setDeleted(articleEntity.getDeleted())
                .setCreatedTime(articleEntity.getCreatedTime())
                .setUpdatedTime(articleEntity.getUpdatedTime());
    }
}
