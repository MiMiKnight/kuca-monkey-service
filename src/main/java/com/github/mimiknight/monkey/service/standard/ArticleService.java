package com.github.mimiknight.monkey.service.standard;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.mimiknight.monkey.model.entity.ArticleEntity;

import java.util.List;

/**
 * Content表服务接口
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-21 17:54:32
 */
public interface ArticleService extends IService<ArticleEntity> {
    List<String> audit(List<String> articleIds, int auditResult);
}
