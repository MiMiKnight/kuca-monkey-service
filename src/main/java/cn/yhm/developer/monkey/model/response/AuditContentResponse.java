package cn.yhm.developer.monkey.model.response;

import cn.yhm.developer.kuca.common.constant.DateTimeFormatStandard;
import cn.yhm.developer.kuca.ecology.model.response.EcologyResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * 内容审核响应
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-07 16:22:42
 */
@Getter
@Setter
public class AuditContentResponse implements EcologyResponse {

    @JsonFormat(pattern = DateTimeFormatStandard.STANDARD_4, timezone = "UTC")
    @JsonProperty(value = "audit_time")
    private ZonedDateTime auditTime;
}
