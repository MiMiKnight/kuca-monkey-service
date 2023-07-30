package com.github.mimiknight.monkey.mapstruct;

import com.github.mimiknight.monkey.model.entity.ArticleEntity;
import com.github.mimiknight.monkey.model.response.QueryArticleByIdResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 公共 MapStruct映射器
 *
 * @author victor2015yhm@163.com
 * @since 2023-07-05 15:53:13
 */
@Mapper
public interface CommonMapStruct {

    CommonMapStruct INSTANCE = Mappers.getMapper(CommonMapStruct.class);

    /**
     * ArticleEntity转QueryContentByIdResponse
     *
     * @param articleEntity 文章表实体类
     * @return {@link QueryArticleByIdResponse}
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "article", target = "article")
    @Mapping(source = "deleted", target = "deleted")
    @Mapping(source = "createdTime", target = "createdTime")
    @Mapping(source = "updatedTime", target = "updatedTime")
    QueryArticleByIdResponse convert(ArticleEntity articleEntity);

}
