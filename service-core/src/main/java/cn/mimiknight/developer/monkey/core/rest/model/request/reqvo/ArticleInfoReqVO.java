package cn.mimiknight.developer.monkey.core.rest.model.request.reqvo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticleInfoReqVO {

    /**
     * tags
     */
    @JsonProperty(value = "tags")
    private List<String> tags;

    /**
     * publisher
     */
    @JsonProperty(value = "publisher")
    private String publisher;
}
