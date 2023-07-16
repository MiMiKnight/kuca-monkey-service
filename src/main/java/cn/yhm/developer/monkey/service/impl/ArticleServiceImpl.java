package cn.yhm.developer.monkey.service.impl;

import cn.yhm.developer.monkey.mapper.ArticleMapper;
import cn.yhm.developer.monkey.model.entity.ArticleEntity;
import cn.yhm.developer.monkey.service.standard.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 文章表服务接口实现类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-21 17:55:25
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ArticleEntity> implements ArticleService {
}
