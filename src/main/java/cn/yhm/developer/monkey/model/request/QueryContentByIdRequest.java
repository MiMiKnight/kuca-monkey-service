package cn.yhm.developer.monkey.model.request;

import cn.yhm.developer.kuca.ecology.model.request.EcologyRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 查询内容请求参数
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-09 20:26:22
 */
@Getter
@Setter
public class QueryContentByIdRequest implements EcologyRequest {

    /**
     * 主键
     */
    @JsonProperty(value = "id")
    private String id;
}
