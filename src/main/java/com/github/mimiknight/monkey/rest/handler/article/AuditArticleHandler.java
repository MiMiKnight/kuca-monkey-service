package com.github.mimiknight.monkey.rest.handler.article;

import com.github.mimiknight.kuca.ecology.core.EcologyRequestHandler;
import com.github.mimiknight.monkey.model.request.AuditArticleRequest;
import com.github.mimiknight.monkey.model.response.AuditArticleResponse;
import com.github.mimiknight.monkey.service.standard.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 保存文章处理器类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-09 20:32:18
 */
@Slf4j
@Component
public class AuditArticleHandler implements EcologyRequestHandler<AuditArticleRequest, AuditArticleResponse> {

    private ArticleService articleService;

    @Autowired
    public void setContentService(ArticleService contentService) {
        this.articleService = contentService;
    }

    @Override
    public void handle(AuditArticleRequest request, AuditArticleResponse response) {
        // 获取文章id集合
        List<String> articleIds = request.getArticleIds();
        // 数据去重
        articleIds = articleIds.stream().distinct().collect(Collectors.toList());
        // 参数校验
        // 审核
        Integer auditResult = request.getAuditResult();
        List<String> auditPassArticleIds = articleService.audit(articleIds, auditResult);
        // 响应赋值
        response.setArticleIds(auditPassArticleIds);
    }
}
