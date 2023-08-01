package com.github.mimiknight.monkey.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Map;

/**
 * API日志格式化
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-08-02 00:14:52
 */
@Slf4j
public class ApiLogFormat {

    private ApiLogFormat() {
    }

    /**
     * 接口请求参数格式化输出
     *
     * @param uri       接口路径
     * @param method    接口请求方式
     * @param headerMap 请求头
     * @param query     请求行参数
     * @param body      请求体
     */
    public static void formatRequest(String uri, String method, Map<String, String> headerMap, Object query, Object body) {
        log.info("===============================Request Begin===============================");
        log.info("URI          : {}", uri);
        log.info("Method       : {}", method);
        log.info("Headers      : {}", headerMap);
        log.info("Request Query: {}", query);
        log.info("Request Body : {}", body);
        log.info("===============================Request End=================================");
    }

    /**
     * 接口响应参数格式化输出
     *
     * @param uri        接口路径
     * @param method     接口请求方式
     * @param headerMap  响应头
     * @param statusCode 响应状态码
     * @param duration   接口响应耗时
     * @param body       响应体
     */
    public static void formatResponse(String uri, String method, Map<String, String> headerMap, int statusCode, Duration duration, Object body) {
        log.info("===============================Response Begin==============================");
        log.info("URI          : {}", uri);
        log.info("Method       : {}", method);
        log.info("Status Code  : {}", statusCode);
        log.info("Cost Time    : {}ms", duration.toMillis());
        log.info("Headers      : {}", headerMap);
        log.info("Response Body: {}", body);
        log.info("===============================Response End================================");
    }
}
