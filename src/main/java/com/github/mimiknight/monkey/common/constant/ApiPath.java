package com.github.mimiknight.monkey.common.constant;

/**
 * 接口路径
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-02 15:24:39
 */
public interface ApiPath {

    /**
     * 项目顶级前缀
     */
    String TOP_PREFIX = "/rest/developer/monkey-service";

    /**
     * 模块路径
     */
    interface Module {

        /**
         * 健康检查模块
         */
        String HEALTH = TOP_PREFIX + "/health";

        /**
         * 文章模块
         */
        String ARTICLE = TOP_PREFIX + "/article";

    }
}
