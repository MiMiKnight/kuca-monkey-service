package com.github.mimiknight.monkey.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.mimiknight.monkey.common.enumeration.ErrorReturn;
import com.github.mimiknight.monkey.common.exception.ServiceException;
import com.github.mimiknight.monkey.mapper.ArticleMapper;
import com.github.mimiknight.monkey.model.entity.ArticleEntity;
import com.github.mimiknight.monkey.service.standard.ArticleService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章表服务接口实现类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-21 17:55:25
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ArticleEntity> implements ArticleService {

    private ArticleMapper articleMapper;

    @Autowired
    public void setArticleMapper(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    @Override
    public List<String> audit(List<String> articleIds, int auditResult) {
        List<ArticleEntity> articleList = articleMapper.selectAuditongArticleByIds(articleIds);
        if (CollectionUtils.isEmpty(articleList)) {
            throw new ServiceException(ErrorReturn.NO_FIND_AUDITING_ARTICLE_BY_IDS);
        }
        List<String> ids = articleList.stream().map(ArticleEntity::getId).collect(Collectors.toList());
        // 审核
        articleMapper.audit(ids, auditResult);
        return ids;
    }
}
