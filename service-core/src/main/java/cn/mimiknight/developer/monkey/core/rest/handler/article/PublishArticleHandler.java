package cn.mimiknight.developer.monkey.core.rest.handler.article;

import cn.mimiknight.developer.kuca.spring.ecology.handler.EcologyRequestHandler;
import cn.mimiknight.developer.monkey.core.rest.model.request.PublishArticleRequest;
import cn.mimiknight.developer.monkey.core.rest.model.response.PublishArticleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PublishArticleHandler implements EcologyRequestHandler<PublishArticleRequest, PublishArticleResponse> {

    @Override
    public PublishArticleResponse handle(PublishArticleRequest request) {
        log.info("request = {}", request);
        return new PublishArticleResponse();
    }
}

