package cn.mimiknight.developer.monkey.core.rest.model.response;

import cn.mimiknight.developer.kuca.spring.validation.annotation.KucaValidated;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 发表文章响应参数
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-08-18 22:39:13
 */
@Setter
@Getter
@KucaValidated
public class PublishArticleResponse {

    @JsonProperty(value = "result", index = 1)
    private String result="success";
}
