package cn.yhm.developer.monkey.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;

/**
 * MybatisPlus配置类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-22 07:51:51
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * MybatisPlus 创建时间、更新时间自动填充
     *
     * @return {@link MetaObjectHandler}
     */
    @Bean
    public MetaObjectHandler myMetaObjectHandler() {
        return new MetaObjectHandler() {

            @Override
            public void insertFill(MetaObject metaObject) {
                ZonedDateTime now = ZonedDateTime.now();
                // 自动插入 创建时间
                this.strictInsertFill(metaObject, "createdTime", () -> now, ZonedDateTime.class);
                // 自动插入 更新时间
                this.strictInsertFill(metaObject, "updatedTime", () -> now, ZonedDateTime.class);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                // 自动更新插入 更新时间
                this.strictUpdateFill(metaObject, "updatedTime", ZonedDateTime::now, ZonedDateTime.class);
            }
        };
    }


    /**
     * MyabtisPlus拦截器配置
     *
     * @return {@link MybatisPlusInterceptor}
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 防全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }
}
