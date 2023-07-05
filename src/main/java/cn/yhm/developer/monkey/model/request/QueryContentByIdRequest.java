package cn.yhm.developer.monkey.model.request;

import cn.yhm.developer.kuca.ecology.model.request.EcologyRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

/**
 * 查询内容请求参数
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-09 20:26:22
 */
@Validated
@Getter
@Setter
public class QueryContentByIdRequest implements EcologyRequest {

    /**
     * 主键
     */
    @Past
    @NotBlank(message = "QueryContentByIdRequest.id.NotBlank")
    @Size(min = 32, max = 64, message = "QueryContentByIdRequest.id.Size")
    @JsonProperty(value = "id")
    private String id;
}
