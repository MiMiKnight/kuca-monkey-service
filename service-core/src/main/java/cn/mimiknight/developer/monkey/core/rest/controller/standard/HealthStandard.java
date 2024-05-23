package cn.mimiknight.developer.monkey.core.rest.controller.standard;

import cn.mimiknight.developer.monkey.core.rest.model.response.HealthCheckResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Health模块Controller接口
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-11-11 09:35:07
 */
@Tag(name = "健康检查模块前端控制器")
public interface HealthStandard {
    @Operation(summary = "健康检查接口", description = "查看项目运行状态")
    HealthCheckResponse check() throws Exception;
}
