package cn.yhm.developer.monkey.rest.handler.content;

import cn.yhm.developer.kuca.ecology.core.EcologyRequestHandler;
import cn.yhm.developer.monkey.model.entity.ContentEntity;
import cn.yhm.developer.monkey.model.request.QueryContentByIdRequest;
import cn.yhm.developer.monkey.model.response.QueryContentByIdResponse;
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
public class QueryContentByIdHandler implements EcologyRequestHandler<QueryContentByIdRequest, QueryContentByIdResponse> {

    @Autowired
    private ContentService contentService;

    @Override
    public void handle(QueryContentByIdRequest request, QueryContentByIdResponse response) throws Exception {
        String id = request.getId();
        ContentEntity contentEntity = contentService.getById(id);
        if (null == contentEntity) {
            throw new RuntimeException("Content is not exist.");
        }
        response.setId(contentEntity.getId());
        response.setContent(contentEntity.getContent());
        response.setVersion(contentEntity.getVersion());
        response.setDeleted(contentEntity.getDeleted());
        response.setCreateTime(contentEntity.getCreatedTime());
        response.setUpdateTime(contentEntity.getUpdatedTime());
    }
}
