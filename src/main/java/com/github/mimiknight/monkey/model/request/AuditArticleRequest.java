package com.github.mimiknight.monkey.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mimiknight.kuca.ecology.model.request.EcologyRequest;
import com.github.mimiknight.kuca.utils.constant.DateTimeFormatStandard;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * 审核文章
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-09 20:26:22
 */
@Getter
@Setter
public class AuditArticleRequest implements EcologyRequest {

    /**
     * 审核结果
     * <p>
     * 1：审核通过
     * <p>
     * 2：审核不通过
     */
    @NotNull(message = "AuditContentRequest.auditResult.NotNull")
    @Range(min = 1, max = 2, message = "AuditContentRequest.auditResult.Range")
    @JsonProperty(value = "audit_result")
    private Integer auditResult;

    /**
     * 审核时间
     * <p>
     * 24小时制 "年-月-日 时:分:秒.毫秒 GMT时区"
     * 格式：
     * <p>
     * 2022-09-04 10:06:39.123 GMT+08:00 【表示 东八区 2022年9月4日10点6分39秒123毫秒】
     */
    @DateTimeFormat(pattern = DateTimeFormatStandard.STANDARD_6)
    @JsonFormat(pattern = DateTimeFormatStandard.STANDARD_6)
    @JsonProperty(value = "audit_time")
    private ZonedDateTime auditTime;
}
