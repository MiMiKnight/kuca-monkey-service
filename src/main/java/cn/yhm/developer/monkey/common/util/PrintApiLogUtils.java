package cn.yhm.developer.monkey.common.util;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 打印api日志工具类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-29 02:05:27
 */
@Slf4j
@Component
public class PrintApiLogUtils {

    /**
     * 跟踪请求
     * <p>
     * ContentType为JSON格式则打印JSON格式的请求体日志
     *
     * @param request         请求
     * @param requestBodyJson json请求体
     */
    public void traceRequest(HttpRequest request, String requestBodyJson) {
        log.info("===============================Request Begin===============================");
        log.info("URI         : {}", request.getURI());
        log.info("Method      : {}", request.getMethod());
        log.info("Headers     : {}", request.getHeaders());
        log.info("Request body: {}", requestBodyJson);
        log.info("===============================Request End=================================");
    }

    /**
     * 跟踪响应
     * <p>
     * ContentType为JSON格式则打印JSON格式的响应体日志
     *
     * @param responseBodyJson json响应主体
     * @throws IOException ioexception
     */
    public void traceResponse(String responseBodyJson) throws IOException {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest servletRequest = servletRequestAttributes.getRequest();
        String requestURI = servletRequest.getRequestURI();
        StringBuffer requestURL = servletRequest.getRequestURL();
        String requestMethod = servletRequest.getMethod();
        HttpServletResponse servletResponse = servletRequestAttributes.getResponse();
        log.info("===============================Response Begin==============================");
        log.info("URI          : {}", "");
        log.info("Cost         : {}ms", servletResponse.getStatus());
        log.info("Status code  : {}", servletResponse.getStatus());
        log.info("Headers      : {}", "");
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
