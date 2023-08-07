package com.github.mimiknight.monkey.rest.controller;

import com.github.mimiknight.kuca.ecology.core.EcologyRequestHandleAdapter;
import com.github.mimiknight.kuca.ecology.model.response.SuccessResponse;
import com.github.mimiknight.monkey.common.constant.ApiPath;
import com.github.mimiknight.monkey.model.request.HealthCheckRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查前端控制器
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-09 00:09:37
 */
@Tag(name = "健康检查模块前端控制器")
@Validated
@RestController
@RequestMapping(path = ApiPath.Module.HEALTH, produces = {MediaType.APPLICATION_JSON_VALUE})
public class HealthController extends EcologyRequestHandleAdapter {

    @Operation(summary = "健康检查接口")
    @GetMapping(path = "/servlet/v1/check")
    public SuccessResponse v1() throws Exception {
        return handle(new HealthCheckRequest());
    }
}
