package cn.yhm.developer.monkey.mapstruct;

import cn.yhm.developer.monkey.model.entity.ContentEntity;
import cn.yhm.developer.monkey.model.response.QueryContentByIdResponse;
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
     * ContentEntity转QueryContentByIdResponse
     *
     * @param contentEntity 内容表实体类
     * @return {@link QueryContentByIdResponse}
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "deleted", target = "deleted")
    @Mapping(source = "createdTime", target = "createdTime")
    @Mapping(source = "updatedTime", target = "updatedTime")
    QueryContentByIdResponse convert(ContentEntity contentEntity);

}
