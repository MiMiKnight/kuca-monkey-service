package com.github.mimiknight.monkey.common.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 公共工具类
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-08-01 22:27:31
 */
public class CommonUtils {

    private CommonUtils() {
    }

    /**
     * uuid
     *
     * @return {@link String}
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取请求行参数
     *
     * @param request 请求
     * @return {@link Object}
     */
    public static Object requestQuery(HttpServletRequest request) {
        // TODO 待完善
        return JsonUtils.createObjectNode();
    }

    /**
     * 是否为application/json内容类型
     *
     * @param contentType 内容类型
     * @return boolean
     */
    public static boolean isJsonContentType(String contentType) {
        if (StringUtils.isBlank(contentType)) {
            return false;
        }
        if (MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(contentType)) {
            return true;
        }
        return false;
    }

    /**
     * HttpServletRequest请求中Content-Type是否为JSON类型
     *
     * @param request 请求
     * @return boolean
     */
    public static boolean isJsonContentType(HttpServletRequest request) {
        return isJsonContentType(request.getContentType());
    }

    /**
     * HttpServletResponse响应中Content-Type是否为JSON类型
     *
     * @param response 响应
     * @return boolean
     */
    public static boolean isJsonContentType(HttpServletResponse response) {
        return isJsonContentType(response.getContentType());
    }

    /**
     * 获取请求体
     * <p>
     * 为什么从HttpServletRequest中直接获取的请求体字符串不直接作为请求体返回，而是要包装成JsonNode对象？
     * <p>
     * 1、因为从HttpServletRequest中直接获取的请求体字符串可能含有格式化信息，不适合直接打印输出；
     * <p>
     * 2、直接日志输出字符串可能不利于日志脱敏，故用JsonNode承接；
     * <p>
     * 3、JsonNode适合承接JSON格式类型的数据，其他对象类型无法满足；
     *
     * @param request 请求
     * @return {@link Object}
     */
    public static Object requestBody(HttpServletRequest request) {
        try {
            String contentType = request.getContentType();
            if (CommonUtils.isJsonContentType(contentType)) {
                String body = IOUtils.toString(request.getReader());
                return JsonUtils.readTree(body);
            }
            return JsonUtils.createObjectNode();
        } catch (Exception e) {
            return JsonUtils.createObjectNode();
        }
    }

    /**
     * 获取响应体
     *
     * @param response 响应
     * @return {@link Object}
     */
    public static Object responseBody(HttpServletResponse response) {
        try {
            String contentType = response.getContentType();
            if (CommonUtils.isJsonContentType(contentType)) {
                // TODO 待完善
                String body = "{\"title\":\"测试响应体\"}";
                return JsonUtils.readTree(body);
            }
            return JsonUtils.createObjectNode();
        } catch (Exception e) {
            return JsonUtils.createObjectNode();
        }
    }

    /**
     * 是否为HttpServletResponse请求对象
     *
     * @param request 请求
     * @return boolean
     */
    public static boolean isHttpServletRequest(ServletRequest request) {
        if (null == request) {
            return false;
        }
        return (request instanceof HttpServletRequest);
    }

    /**
     * 是否为HttpServletResponse响应对象
     *
     * @param response 响应
     * @return boolean
     */
    public static boolean isHttpServletResponse(ServletResponse response) {
        if (null == response) {
            return false;
        }
        return (response instanceof HttpServletResponse);
    }

}
