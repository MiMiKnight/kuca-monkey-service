package cn.yhm.developer.monkey.service.impl;

import cn.yhm.developer.monkey.mapper.ContentMapper;
import cn.yhm.developer.monkey.model.entity.ContentEntity;
import cn.yhm.developer.monkey.service.standard.ContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Content表服务接口实现类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-21 17:55:25
 */
@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, ContentEntity> implements ContentService {
}
