package cn.yhm.developer.monkey.mapper;

import cn.yhm.developer.monkey.model.entity.ContentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 内容表持久化类
 * <p>
 * query：查询
 * remove：删除
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-22 07:33:51
 */
@Mapper
public interface ContentMapper extends BaseMapper<ContentEntity> {

}
