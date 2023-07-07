package cn.yhm.developer.monkey.model.request;

import cn.yhm.developer.kuca.ecology.model.request.EcologyRequest;
import cn.yhm.developer.kuca.validation.annotation.validation.ValidateNotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 修改内容请求参数
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-09 20:26:22
 */
@Getter
@Setter
public class ModifyContentByIdRequest implements EcologyRequest {

    /**
     * 主键
     */
    @NotBlank(message = "ModifyContentByIdRequest.id.NotBlank")
    @Size(min = 18, max = 64, message = "ModifyContentByIdRequest.id.Size")
    @JsonProperty(value = "id")
    private String id;

    /**
     * 内容
     */
    @NotBlank(message = "ModifyContentByIdRequest.content.NotBlank")
    @Size(min = 5, max = 20000, message = "ModifyContentByIdRequest.content.Size")
    @JsonProperty(value = "content")
    private String content;
}
