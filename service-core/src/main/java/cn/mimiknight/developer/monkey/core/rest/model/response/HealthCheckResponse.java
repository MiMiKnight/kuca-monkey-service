package cn.mimiknight.developer.monkey.core.rest.model.response;

import cn.mimiknight.developer.kuca.spring.validation.annotation.KucaValidated;
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
@KucaValidated
public class HealthCheckResponse {

    /**
     * 服务名称
     */
    @JsonProperty(value = "service_name", index = 1)
    private String serviceName;

    /**
     * 数据库状态
     */
    @JsonProperty(value = "database_status", index = 2)
    private boolean databaseStatus = false;

    /**
     * redis状态
     */
    @JsonProperty(value = "redis_status", index = 3)
    private boolean redisStatus = false;
}
