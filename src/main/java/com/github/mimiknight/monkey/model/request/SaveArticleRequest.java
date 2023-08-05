package com.github.mimiknight.monkey.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mimiknight.kuca.ecology.model.request.EcologyRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * 保存文章内容请求参数
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-09 20:26:22
 */
@Getter
@Setter
public class SaveArticleRequest implements EcologyRequest {

    /**
     * 文章标题
     */
    @NotBlank(message = "SaveArticleRequest.title.NotBlank")
    @Length(min = 5, max = 128, message = "SaveArticleRequest.title.Length")
    @JsonProperty(value = "title")
    private String title;

    /**
     * 文章内容
     */
    @NotBlank(message = "SaveArticleRequest.article.NotBlank")
    @Length(min = 10, max = 512, message = "SaveArticleRequest.article.Length")
    @JsonProperty(value = "article")
    private String article;
}
