package com.github.mimiknight.monkey.rest.controller;

import com.github.mimiknight.kuca.ecology.core.EcologyRequestHandleAdapter;
import com.github.mimiknight.kuca.ecology.model.response.SuccessResponse;
import com.github.mimiknight.monkey.common.constant.ApiPath;
import com.github.mimiknight.monkey.model.request.AuditArticleRequest;
import com.github.mimiknight.monkey.model.request.ModifyArticleByIdRequest;
import com.github.mimiknight.monkey.model.request.QueryArticleByIdRequest;
import com.github.mimiknight.monkey.model.request.SaveArticleRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Article模块前端控制器
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-07 16:18:26
 */
@Tag(name = "Article模块前端控制器")
@Validated
@RestController
@RequestMapping(path = ApiPath.Module.ARTICLE, produces = {MediaType.APPLICATION_JSON_VALUE})
public class ArticleController extends EcologyRequestHandleAdapter {

    @Operation(summary = "保存article接口")
    @PostMapping(value = "/user/v1/save")
    public SuccessResponse v1(@RequestBody SaveArticleRequest request) throws Exception {
        return handle(request);
    }

    @Operation(summary = "修改article接口")
    @PostMapping(value = "/user/v1/modify")
    public SuccessResponse v1(@RequestBody ModifyArticleByIdRequest request) throws Exception {
        return handle(request);
    }

    @Operation(summary = "根据id查询article接口")
    @GetMapping(value = "/user/v1/query")
    public SuccessResponse v1(@RequestParam("id") String id) throws Exception {
        QueryArticleByIdRequest request = new QueryArticleByIdRequest();
        request.setId(id);
        return handle(request);
    }

    @Operation(summary = "文章审核接口")
    @PostMapping(value = "/user/v1/audit")
    public SuccessResponse v1(@RequestBody AuditArticleRequest request) throws Exception {
        return handle(request);
    }
}
