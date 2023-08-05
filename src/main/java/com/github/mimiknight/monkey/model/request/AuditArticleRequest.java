package com.github.mimiknight.monkey.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mimiknight.kuca.ecology.model.request.EcologyRequest;
import com.github.mimiknight.kuca.utils.constant.DateTimeFormatStandard;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * 文章审核接口请求参数对象
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-09 20:26:22
 */
@Data
public class AuditArticleRequest implements EcologyRequest {

    /**
     * 文章id集合
     */
    @Size(min = 1, max = 64, message = "AuditArticleRequest.articleIds.Size")
    @JsonProperty(value = "article_ids")
    private List<String> articleIds;

    /**
     * 审核结果
     * <p>
     * 1：审核通过
     * <p>
     * 2：审核不通过
     */
    @NotNull(message = "AuditArticleRequest.auditResult.NotNull")
    @Range(min = 2, max = 3, message = "AuditArticleRequest.auditResult.Range")
    @JsonProperty(value = "audit_result")
    private Integer auditResult;

    /**
     * 审核时间
     * <p>
     * 格式：
     * <p>
     * 24小时制 "年-月-日 时:分:秒.毫秒 时区"
     * <p>
     * 案例：
     * <p>
     * 2022-09-04 10:06:39.123 +08:00 【表示 东八区 2022年9月4日10点6分39秒123毫秒】
     */
    @DateTimeFormat(pattern = DateTimeFormatStandard.STANDARD_6)
    @JsonFormat(pattern = DateTimeFormatStandard.STANDARD_6)
    @JsonProperty(value = "audit_time")
    private ZonedDateTime auditTime;
}
