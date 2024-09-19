package cn.mimiknight.developer.monkey.core.rest.model.request.reqvo;

import cn.mimiknight.developer.kuca.spring.validation.annotation.KucaValidated;
import cn.mimiknight.developer.kuca.spring.validation.annotation.validation.KucaLength;
import cn.mimiknight.developer.kuca.spring.validation.annotation.validation.KucaNotEmpty;
import cn.mimiknight.developer.kuca.spring.validation.annotation.validation.KucaNotNull;
import cn.mimiknight.developer.kuca.spring.validation.annotation.validation.KucaSize;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@KucaValidated
public class ArticleInfoReqVO {

    /**
     * tags
     */
    @KucaNotNull
    @KucaSize(min = 1, max = 20)
    @JsonProperty(value = "tags")
    private List<String> tags;

    /**
     * publisher
     */
    @KucaNotEmpty
    @KucaLength(min = 1, max = 64)
    @JsonProperty(value = "publisher")
    private String publisher;
}
