package cn.mimiknight.developer.monkey.core.rest.controller.standard;

import cn.mimiknight.developer.kuca.spring.appeasy.model.response.VoidResponse;
import cn.mimiknight.developer.monkey.core.rest.model.request.PublishArticleRequest;

/**
 * Health模块Controller接口
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-11-11 09:35:07
 */
public interface ArticleStandard {

    VoidResponse publish(PublishArticleRequest request) throws Exception;
}
