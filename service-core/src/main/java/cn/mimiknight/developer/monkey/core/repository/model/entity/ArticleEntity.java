package cn.mimiknight.developer.monkey.core.repository.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * <p>
 * 文章表
 * <p>
 * t_monkey_article
 * </p>
 *
 * @author victor2015yhm@gmail.com
 * @since 2024-01-18 23:11:19
 */
@Getter
@Setter
@Accessors(chain = true)
public class ArticleEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String article;

    /**
     * 逻辑删除
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    private ZonedDateTime createdTime;

    /**
     * 更新时间
     */
    private ZonedDateTime updatedTime;
}
