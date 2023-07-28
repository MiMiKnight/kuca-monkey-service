package com.github.mimiknight.monkey.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mimiknight.kuca.ecology.model.request.EcologyRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 修改文章请求参数
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-09 20:26:22
 */
@Data
public class ModifyArticleByIdRequest implements EcologyRequest {

    /**
     * 主键
     */
    @NotBlank(message = "ModifyContentByIdRequest.id.NotBlank")
    @Size(min = 18, max = 64, message = "ModifyContentByIdRequest.id.Size")
    @JsonProperty(value = "id")
    private String id;

    /**
     * 文章标题
     */
    @JsonProperty(value = "title")
    private String title;

    /**
     * 文章内容
     */
    @NotBlank(message = "ModifyContentByIdRequest.content.NotBlank")
    @Size(min = 5, max = 20000, message = "ModifyContentByIdRequest.content.Size")
    @JsonProperty(value = "article")
    private String article;
}
