package cn.yhm.developer.monkey.rest.handler.content;

import cn.yhm.developer.kuca.ecology.core.EcologyRequestHandler;
import cn.yhm.developer.monkey.model.entity.ContentEntity;
import cn.yhm.developer.monkey.model.request.AuditContentRequest;
import cn.yhm.developer.monkey.model.response.AuditContentResponse;
import cn.yhm.developer.monkey.service.standard.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

/**
 * 审核内容处理器类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-09 20:32:18
 */
@Component
public class AuditContentHandler implements EcologyRequestHandler<AuditContentRequest, AuditContentResponse> {

    @Autowired
    private ContentService contentService;

    @Override
    public void handle(AuditContentRequest request, AuditContentResponse response) throws Exception {
        ZonedDateTime auditTime = request.getAuditTime();
        response.setAuditTime(auditTime);
        ContentEntity contentEntity = new ContentEntity();
        contentEntity.setContent("测试内容");
        contentEntity.setDeleted(0);
        contentEntity.setVersion(1);
        contentService.save(contentEntity);
    }
}
