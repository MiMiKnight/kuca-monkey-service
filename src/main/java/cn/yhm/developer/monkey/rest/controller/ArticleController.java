package cn.yhm.developer.monkey.rest.controller;

import cn.yhm.developer.kuca.ecology.core.EcologyRequestHandleAdapter;
import cn.yhm.developer.kuca.ecology.model.response.SuccessResponse;
import cn.yhm.developer.monkey.common.constant.ApiPath;
import cn.yhm.developer.monkey.model.request.ModifyArticleByIdRequest;
import cn.yhm.developer.monkey.model.request.QueryArticleByIdRequest;
import cn.yhm.developer.monkey.model.request.SaveArticleRequest;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Content模块前端控制器
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-07 16:18:26
 */
@Validated
@RestController
@RequestMapping(path = ApiPath.Module.ARTICLE, produces = {MediaType.APPLICATION_JSON_VALUE})
public class ArticleController extends EcologyRequestHandleAdapter {

    @PostMapping(value = "/v1/save")
    public SuccessResponse v1(@RequestBody SaveArticleRequest request) throws Exception {
        return handle(request);
    }

    @PostMapping(value = "/v1/modify")
    public SuccessResponse v1(@RequestBody ModifyArticleByIdRequest request) throws Exception {
        return handle(request);
    }

    @GetMapping(value = "/v1/query")
    public SuccessResponse v1(@RequestParam("id") String id) throws Exception {
        QueryArticleByIdRequest request = new QueryArticleByIdRequest();
        request.setId(id);
        return handle(request);
    }
}
