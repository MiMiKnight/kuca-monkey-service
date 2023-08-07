package com.github.mimiknight.monkey.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mimiknight.kuca.ecology.model.request.EcologyRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
    @NotBlank(message = "ModifyArticleByIdRequest.id.NotBlank")
    @Length(min = 18, max = 64, message = "ModifyArticleByIdRequest.id.Length")
    @JsonProperty(value = "id")
    private String id;

    /**
     * 文章标题
     */
    @NotBlank(message = "ModifyArticleByIdRequest.title.NotBlank")
    @Length(min = 5, max = 128, message = "ModifyArticleByIdRequest.title.Length")
    @JsonProperty(value = "title")
    private String title;

    /**
     * 文章内容
     */
    @NotBlank(message = "ModifyArticleByIdRequest.article.NotBlank")
    @Length(min = 10, max = 512, message = "ModifyArticleByIdRequest.article.Length")
    @JsonProperty(value = "article")
    private String article;
}
