package com.github.mimiknight.monkey.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.mimiknight.monkey.model.entity.ArticleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
@Repository
@Mapper
public interface ArticleMapper extends BaseMapper<ArticleEntity> {

    /**
     * 根据文章主键查找审核中的文章
     *
     * @param articleIds 文章主键id集合
     * @return {@link List}<{@link ArticleEntity}>
     */
    List<ArticleEntity> selectAuditongArticleByIds(@Param("articleIds") List<String> articleIds);

    /**
     * 文章审核
     *
     * @param articleIds  文章主键id集合
     * @param auditResult 审核状态
     */
    void audit(@Param("articleIds") List<String> articleIds, @Param("auditResult") int auditResult);
}
