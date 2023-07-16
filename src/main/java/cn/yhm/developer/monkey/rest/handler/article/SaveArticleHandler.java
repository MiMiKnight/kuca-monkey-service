package cn.yhm.developer.monkey.rest.handler.article;

import cn.yhm.developer.kuca.ecology.core.EcologyRequestHandler;
import cn.yhm.developer.monkey.model.entity.ArticleEntity;
import cn.yhm.developer.monkey.model.request.SaveArticleRequest;
import cn.yhm.developer.monkey.model.response.SaveArticleResponse;
import cn.yhm.developer.monkey.service.standard.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 保存内容处理器类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-09 20:32:18
 */
@Component
public class SaveArticleHandler implements EcologyRequestHandler<SaveArticleRequest, SaveArticleResponse> {

    private ArticleService contentService;

    @Autowired
    public void setContentService(ArticleService contentService) {
        this.contentService = contentService;
    }

    @Override
    public void handle(SaveArticleRequest request, SaveArticleResponse response) throws Exception {
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(request.getTitle());
        entity.setArticle(request.getArticle());
        contentService.save(entity);
    }
}
