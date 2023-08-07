package com.github.mimiknight.monkey.model.entity;

import lombok.Data;

import java.time.ZonedDateTime;

/**
 * <p>
 * 文章表 t_monkey_article
 * </p>
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-06-05 06:42:47
 */
@Data
public class ArticleEntity {

    private static final long serialVersionUID = 123080239902732129L;

    /**
     * 主键 id
     */
    private String id;

    /**
     * 标题 title
     */
    private String title;

    /**
     * 文章内容 article
     */
    private String article;

    /**
     * 审核 audit
     * <p>
     * 1：审核中
     * <p>
     * 2：审核通过
     * <p>
     * 3：审核不通过
     */
    private Integer audit;

    /**
     * 逻辑删除 deleted
     */
    private Integer deleted;

    /**
     * 创建时间 createdTime
     */
    private ZonedDateTime createdTime;

    /**
     * 更新时间 updatedTime
     */
    private ZonedDateTime updatedTime;

}
