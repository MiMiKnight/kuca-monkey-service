package cn.mimiknight.developer.monkey.core.rest.model.request;

import cn.mimiknight.developer.kuca.spring.ecology.model.request.EcologyRequest;
import cn.mimiknight.developer.monkey.core.rest.model.request.reqvo.ArticleInfoReqVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 发表文章请求参数
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-08-18 22:39:13
 */
@Getter
@Setter
public class PublishArticleRequest implements EcologyRequest {

    /**
     * 文章标题
     */
    @JsonProperty(value = "title")
    private String title;

    /**
     * 文章作者
     */
    @JsonProperty(value = "author")
    private String author;

    /**
     * 文章内容
     */
    @JsonProperty(value = "content")
    private String content;

    /**
     * 文章信息
     */
    @JsonProperty(value = "info")
    private ArticleInfoReqVO info;

}
