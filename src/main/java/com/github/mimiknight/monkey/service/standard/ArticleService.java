package com.github.mimiknight.monkey.service.standard;

import com.github.mimiknight.monkey.model.entity.ArticleEntity;

import java.util.List;

/**
 * 文章表服务接口
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-21 17:54:32
 */
public interface ArticleService {
    List<String> audit(List<String> articleIds, int auditResult);

    ArticleEntity getEntityById(String id);

    void updateArticleById(ArticleEntity entity);

    void save(String title,String article);
}
