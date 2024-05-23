package cn.mimiknight.developer.monkey.core.rest.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 健康检查响应参数
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-08-18 22:39:13
 */
@Setter
@Getter
public class HealthCheckResponse {

    @JsonProperty(value = "service_name", index = 1)
    private String serviceName;
}
