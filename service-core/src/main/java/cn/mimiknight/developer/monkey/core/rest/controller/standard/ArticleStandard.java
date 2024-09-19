package cn.mimiknight.developer.monkey.core.rest.controller.standard;

import cn.mimiknight.developer.monkey.core.rest.model.request.PublishArticleRequest;
import cn.mimiknight.developer.monkey.core.rest.model.response.PublishArticleResponse;

/**
 * Health模块Controller接口
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-11-11 09:35:07
 */
public interface ArticleStandard {

    PublishArticleResponse publish(PublishArticleRequest request) throws Exception;
}
