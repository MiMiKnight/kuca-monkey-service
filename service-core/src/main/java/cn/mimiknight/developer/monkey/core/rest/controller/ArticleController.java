package cn.mimiknight.developer.monkey.core.rest.controller;

import cn.mimiknight.developer.kuca.spring.ecology.AbstractEcologyRequestController;
import cn.mimiknight.developer.monkey.core.rest.controller.standard.AppApiPath;
import cn.mimiknight.developer.monkey.core.rest.controller.standard.ArticleStandard;
import cn.mimiknight.developer.monkey.core.rest.model.request.PublishArticleRequest;
import cn.mimiknight.developer.monkey.core.rest.model.response.PublishArticleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文章模块前端控制器
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-08-18 22:39:13
 */
@Slf4j
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping(path = AppApiPath.Module.ARTICLE)
public class ArticleController extends AbstractEcologyRequestController implements ArticleStandard {

    @PostMapping(path = "/user/v1/publish-article",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public PublishArticleResponse publish(@RequestBody PublishArticleRequest request) throws Exception {
        return handle(request, PublishArticleResponse.class);
    }
}
