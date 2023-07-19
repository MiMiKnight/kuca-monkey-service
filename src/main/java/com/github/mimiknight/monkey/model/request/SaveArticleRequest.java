package com.github.mimiknight.monkey.model.request;

import cn.yhm.developer.kuca.ecology.model.request.EcologyRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

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
    @JsonProperty(value = "title")
    private String title;

    /**
     * 文章内容
     */
    @JsonProperty(value = "article")
    private String article;
}
