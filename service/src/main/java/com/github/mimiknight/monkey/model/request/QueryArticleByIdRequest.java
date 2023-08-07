package com.github.mimiknight.monkey.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mimiknight.kuca.ecology.model.request.EcologyRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

/**
 * 查询内容请求参数
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-09 20:26:22
 */
@Validated
@Getter
@Setter
public class QueryArticleByIdRequest implements EcologyRequest {

    /**
     * 主键
     */
    @NotBlank(message = "QueryArticleByIdRequest.id.NotBlank")
    @Length(min = 18, max = 64, message = "QueryArticleByIdRequest.id.Length")
    @JsonProperty(value = "id")
    private String id;
}
