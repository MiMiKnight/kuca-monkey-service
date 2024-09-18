package cn.mimiknight.developer.monkey.core.rest.controller.standard;

/**
 * 项目接口路径
 * <p>
 * 接口命名规则：项目前缀+模块名称+接口级别+接口版本+接口名称
 * <p>
 * 项目前缀命名规则：/rest/项目团队代号/项目代号
 * <p>
 * 接口级别命名规则：/servlet（非用户登陆接口）；/user（用户级接口）；/mng(管理台接口)
 * <p>
 * 接口版本命名规则：/v1（该接口的第1个版本）；/v2（该接口的第2个版本）；以此类推
 * <p>
 * 案例：
 * <p>
 * 项目前缀：/rest/developer/monkey-service
 * <p>
 * 项目模块：/article
 * <p>
 * 接口级别：/user
 * <p>
 * 接口版本：/v1
 * <p>
 * 接口名称：/publish-article
 * <p>
 * 完整接口路径：/rest/developer/monkey-service/article/user/v1/publish-article
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-08-18 22:39:13
 */
public interface AppApiPath {

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
