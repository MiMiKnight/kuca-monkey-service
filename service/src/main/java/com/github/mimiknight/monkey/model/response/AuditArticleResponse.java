package com.github.mimiknight.monkey.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mimiknight.kuca.ecology.model.response.EcologyResponse;
import lombok.Data;

import java.util.List;


/**
 * 文章审核接口响应参数对象
 *
 * @author MiMiKnight <victor2015yhm@gmail.com>
 * @since 2023-07-30 09:31:37
 */
@Data
public class AuditArticleResponse implements EcologyResponse {

    /**
     * 经过审核的文章id集合
     */
    @JsonProperty(value = "article_ids")
    private List<String> articleIds;

}
