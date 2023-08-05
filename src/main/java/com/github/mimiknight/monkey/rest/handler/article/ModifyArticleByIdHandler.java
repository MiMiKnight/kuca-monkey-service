package com.github.mimiknight.monkey.rest.handler.article;

import com.github.mimiknight.kuca.ecology.core.EcologyRequestHandler;
import com.github.mimiknight.kuca.utils.service.standard.RedisService;
import com.github.mimiknight.monkey.common.constant.RedisCacheKey;
import com.github.mimiknight.monkey.common.constant.RedisLockKey;
import com.github.mimiknight.monkey.common.enumeration.ErrorReturn;
import com.github.mimiknight.monkey.common.exception.ServiceException;
import com.github.mimiknight.monkey.common.utils.LockCodeUtils;
import com.github.mimiknight.monkey.model.entity.ArticleEntity;
import com.github.mimiknight.monkey.model.request.ModifyArticleByIdRequest;
import com.github.mimiknight.monkey.model.response.ModifyArticleByIdResponse;
import com.github.mimiknight.monkey.service.standard.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 保存文章处理器类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-09 20:32:18
 */
@Slf4j
@Component
public class ModifyArticleByIdHandler implements EcologyRequestHandler<ModifyArticleByIdRequest, ModifyArticleByIdResponse> {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RedisService redisService;

    @Override
    public void handle(ModifyArticleByIdRequest request, ModifyArticleByIdResponse response) {
        // 为业务逻辑加锁
        String lockName = RedisLockKey.ARTICLE_TABLE_LOCK_KEY_PREFIX + request.getId();
        LockCodeUtils.doTryLock(lockName, () -> {
            String id = request.getId();
            String title = request.getTitle();
            String article = request.getArticle();
            ArticleEntity entity = articleService.getEntityById(id);
            if (null == entity) {
                String tip = String.format("Article is not exist,id = %s", id);
                log.info(tip);
                throw new ServiceException(ErrorReturn.ARTICLE_DO_NOT_EXIST, tip);
            }
            entity.setTitle(title);
            entity.setArticle(article);
            articleService.updateArticleById(entity);
            // 清除缓存
            String cacheKey = RedisCacheKey.ARTICLE_TABLE_CACHE_KEY_PREFIX + id;
            redisService.delete(cacheKey);
        });
    }
}
