package com.github.mimiknight.monkey.mapper;

import com.github.mimiknight.monkey.model.entity.ArticleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * 文章表持久化类
 * <p>
 * query：查询
 * remove：删除
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-22 07:33:51
 */
@Mapper
public interface ArticleMapper {

    /**
     * 根据文章主键查找审核中的文章
     *
     * @param articleIds 文章主键id集合
     * @return {@link List}<{@link ArticleEntity}>
     */
    List<ArticleEntity> selectAuditingArticleByIds(@Param("articleIds") List<String> articleIds);

    /**
     * 文章审核
     *
     * @param articleIds  文章主键id集合
     * @param auditResult 审核状态
     */
    void audit(@Param("articleIds") List<String> articleIds, @Param("auditResult") int auditResult);

    ArticleEntity getEntityById(@Param("id") String id);

    void updateArticleById(@Param("id") String id,
                           @Param("title") String title,
                           @Param("article") String article,
                           @Param("updatedTime") ZonedDateTime updatedTime);

    void save(@Param("id") String id,
              @Param("title") String title,
              @Param("article") String article,
              @Param("createdTime") ZonedDateTime createdTime,
              @Param("updatedTime") ZonedDateTime updatedTime);
}
