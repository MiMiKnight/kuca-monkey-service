package cn.yhm.developer.monkey.common.util.impl;

import cn.yhm.developer.kuca.common.utils.standard.JsonService;
import cn.yhm.developer.monkey.common.util.standard.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.TreeMap;

/**
 * 日志工具类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-07-07 21:50:40
 */
@Slf4j
@Component
public class
  LogUtilsImpl implements LogUtils {

    private JsonService jsonService;

    @Autowired
    public void setJsonService(JsonService jsonService) {
        this.jsonService = jsonService;
    }

    @Override
    public void traceRequest(HttpServletRequest servletRequest, Object requestParam) throws IOException {
        String uri = servletRequest.getRequestURI();
        String method = servletRequest.getMethod();
        String queryString = servletRequest.getQueryString();
        String characterEncoding = servletRequest.getCharacterEncoding();

        HttpServletRequestWrapper servletRequestWrapper = new HttpServletRequestWrapper(servletRequest);




        ServletInputStream inputStream = servletRequest.getInputStream();
        byte[] requestBodyBytes = IOUtils.toByteArray(inputStream);


        TreeMap<String, String> requestHeaderMap = new TreeMap<>();
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = servletRequest.getHeader(name);
            requestHeaderMap.put(name, value);
        }

        String headers = jsonService.toJson(requestHeaderMap);
        String requestBodyJson = jsonService.toJson(requestParam);
        String requestParamJson = "";
        log.info("===============================Request Begin===============================");
        log.info("URI          : {}", uri);
        log.info("Method       : {}", method);
        log.info("Headers      : {}", headers);
        log.info("Request Query: {}", requestParamJson);
        log.info("Request Body : {}", requestBodyJson);
        log.info("===============================Request End=================================");
    }

    @Override
    public void traceResponse(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                              Object responseBody) {
        Integer httpStatusCode = servletResponse.getStatus();
        String uri = servletRequest.getRequestURI();
        String method = servletRequest.getMethod();

        TreeMap<String, String> responseHeaderMap = new TreeMap<>();
        Collection<String> headerNames = servletResponse.getHeaderNames();
        for (String name : headerNames) {
            String value = servletResponse.getHeader(name);
            responseHeaderMap.put(name, value);
        }

        String headers = jsonService.toJson(responseHeaderMap);
        String responseBodyJson = jsonService.toJson(responseBody);
        log.info("===============================Response Begin==============================");
        log.info("URI          : {}", uri);
        log.info("Method       : {}", method);
        log.info("Status code  : {}", httpStatusCode);
        log.info("Headers      : {}", headers);
        log.info("Response body: {}", responseBodyJson);
        log.info("===============================Response End================================");
    }

    /**
     * 判断本次请求的数据类型是否为json
     *
     * @param contentType 内容类型
     * @return boolean
     */
    private boolean contentTypeIsJson(String contentType) {
        if (StringUtils.isBlank(contentType)) {
            return false;
        }
        return contentType.contains(MediaType.APPLICATION_JSON_VALUE);
    }
}
