package cn.yhm.developer.monkey.common.enumeration;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 错误类型
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-10 07:47:36
 */
@Getter
public enum ErrorType {


    /**
     * 默认异常
     * <p>
     * 500
     */
    DEFAULT_EXCEPTION("系统默认异常", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * 参数校验异常
     * <p>
     * 400
     */
    PARAMETER_VALID_FAILED("参数校验异常", HttpStatus.BAD_REQUEST),

    /**
     * 资源未找到异常
     * <p>
     * 404
     */
    RESOURCE_NOT_FOUND("资源未找到异常", HttpStatus.NOT_FOUND),

    /**
     * 请求方法不允许异常
     * <p>
     * 405
     */
    METHOD_NOT_ALLOWED("请求方法不允许异常", HttpStatus.METHOD_NOT_ALLOWED),

    /**
     * 业务异常
     * <p>
     * 500
     */
    SERVICE_EXCEPTION("业务异常", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * 调用第三方接口（类库）异常
     */
    CALL_THIRD_PARTY_EXCEPTION("调用第三方接口异常", HttpStatus.NOT_IMPLEMENTED);


    /**
     * 异常类型名称
     */
    private final String name;

    /**
     * HTTP状态码
     */
    private final int httpStatusCode;

    /**
     * 构造方法
     *
     * @param name           异常类型名称
     * @param httpStatusCode HTTP状态码
     */
    ErrorType(String name, int httpStatusCode) {
        this.name = name;
        this.httpStatusCode = httpStatusCode;
    }

    /**
     * 构造方法
     *
     * @param name 异常类型名称
     */
    ErrorType(String name) {
        this(name, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * 构造方法
     *
     * @param name       异常类型名称
     * @param httpStatus HTTP状态码 {@link HttpStatus}
     */
    ErrorType(String name, HttpStatus httpStatus) {
        this(name, httpStatus.value());
    }
}
