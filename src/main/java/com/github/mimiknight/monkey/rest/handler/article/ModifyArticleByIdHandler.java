package com.github.mimiknight.monkey.rest.handler.article;

import com.github.mimiknight.kuca.ecology.core.EcologyRequestHandler;
import com.github.mimiknight.kuca.utils.service.standard.RedisLockService;
import com.github.mimiknight.monkey.common.enumeration.ErrorReturn;
import com.github.mimiknight.monkey.common.exception.ServiceException;
import com.github.mimiknight.monkey.common.utils.standard.LockService;
import com.github.mimiknight.monkey.model.entity.ArticleEntity;
import com.github.mimiknight.monkey.model.request.ModifyArticleByIdRequest;
import com.github.mimiknight.monkey.model.response.ModifyArticleByIdResponse;
import com.github.mimiknight.monkey.service.standard.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 保存文章处理器类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-09 20:32:18
 */
@Component
public class ModifyArticleByIdHandler implements EcologyRequestHandler<ModifyArticleByIdRequest, ModifyArticleByIdResponse> {


    private ArticleService articleService;

    @Autowired
    public void setContentService(ArticleService contentService) {
        this.articleService = contentService;
    }

    private RedisLockService redisLockService;

    @Autowired
    public void setRedisLockService(RedisLockService redisLockService) {
        this.redisLockService = redisLockService;
    }

    private LockService lockService;

    @Autowired
    public void setLockService(LockService lockService) {
        this.lockService = lockService;
    }

    @Override
    public void handle(ModifyArticleByIdRequest request, ModifyArticleByIdResponse response) {
        String id = request.getId();
        String title = request.getTitle();
        String article = request.getArticle();
        ArticleEntity entity = articleService.getById(id);

        if (null == entity) {
            String tip = String.format("id = %s ,content is not exist.", id);
            throw new ServiceException(ErrorReturn.ARTICLE_DO_NOT_EXIST, tip);
        }
        // 为业务逻辑加锁
        String lockName = "MonkeyService:Lock:ModifyContentTable:" + id;
        lockService.doTryLock(lockName, () -> {
            entity.setTitle(title);
            entity.setArticle(article);
            articleService.updateById(entity);
        });
    }
}
