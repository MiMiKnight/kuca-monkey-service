package cn.yhm.developer.monkey.rest.handler.content;

import cn.yhm.developer.kuca.ecology.core.EcologyRequestHandler;
import cn.yhm.developer.monkey.model.entity.ContentEntity;
import cn.yhm.developer.monkey.model.request.SaveContentRequest;
import cn.yhm.developer.monkey.model.response.SaveContentResponse;
import cn.yhm.developer.monkey.service.standard.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 保存内容处理器类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-09 20:32:18
 */
@Component
public class SaveContentHandler implements EcologyRequestHandler<SaveContentRequest, SaveContentResponse> {

    @Autowired
    private ContentService contentService;

    @Override
    public void handle(SaveContentRequest request, SaveContentResponse response) throws Exception {
        ContentEntity entity = new ContentEntity();
        entity.setContent(request.getContent());
        contentService.save(entity);
    }
}
