package cn.mimiknight.developer.monkey.core.rest.handler.article;

import cn.mimiknight.developer.kuca.spring.appeasy.model.response.VoidResponse;
import cn.mimiknight.developer.kuca.spring.ecology.handler.KucaEcologyRequestHandler;
import cn.mimiknight.developer.monkey.core.rest.model.request.PublishArticleRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PublishArticleHandler implements KucaEcologyRequestHandler<PublishArticleRequest, VoidResponse> {

    @Override
    public VoidResponse handle(PublishArticleRequest request) {
        log.info("request = {}", request);
        return VoidResponse.create();
    }
}

