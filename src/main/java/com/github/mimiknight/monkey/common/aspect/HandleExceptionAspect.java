package com.github.mimiknight.monkey.common.aspect;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mimiknight.kuca.ecology.model.response.ExceptionResponse;
import com.github.mimiknight.monkey.common.enumeration.ErrorReturn;
import com.github.mimiknight.monkey.common.exception.ServiceException;
import com.github.mimiknight.monkey.common.tip.ErrorFieldTip;
import com.github.mimiknight.monkey.common.tip.ErrorTip;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.time.ZonedDateTime;
import java.util.Set;

/**
 * 全局异常处理切面
 *
 * @author victor2015yhm@gmail.com
 * @since 2022-09-04 21:42:34
 */
@Slf4j
@Component
@RestControllerAdvice
public class HandleExceptionAspect {
    @Autowired
    private HttpServletResponse servletResponse;

    /**
     * 默认异常处理
     * <p>
     * 500
     *
     * @param e 异常类型 {@link Exception}
     * @return {@link ExceptionResponse}<{@link ?}>
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ExceptionResponse handle(Exception e) {
        // 错误返回信息
        ErrorReturn errorReturn = ErrorReturn.DEFAULT_EXCEPTION;
        // 设置ErrorTip
        ErrorTip errorTip = ErrorTip.build("System unknown error.");
        // 获取状态码
        int httpStatusCode = errorReturn.getErrorType().getHttpStatusCode();
        // 设置状态码
        servletResponse.setStatus(httpStatusCode);
        return buildExceptionResponse(errorReturn, errorTip);
    }


    /**
     * 接口请求方法不允许异常
     *
     * @param e 异常类型 {@link HttpRequestMethodNotSupportedException}
     * @return {@link ExceptionResponse}
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ExceptionResponse handle(HttpRequestMethodNotSupportedException e) {
        // 错误返回信息
        ErrorReturn errorReturn = ErrorReturn.METHOD_NOT_ALLOWED;
        // 设置ErrorTip
        ErrorTip errorTip = ErrorTip.build("Request method is not allowed.");
        // 获取状态码
        int httpStatusCode = errorReturn.getErrorType().getHttpStatusCode();
        // 设置状态码
        servletResponse.setStatus(httpStatusCode);
        return buildExceptionResponse(errorReturn, errorTip);
    }

    /**
     * 资源未找到异常
     *
     * @param e 异常类型 {@link NoHandlerFoundException}
     * @return {@link ExceptionResponse}
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ExceptionResponse handle(NoHandlerFoundException e) {
        // 错误返回信息
        ErrorReturn errorReturn = ErrorReturn.RESOURCE_NOT_FOUND;
        // 设置ErrorTip
        ErrorTip errorTip = ErrorTip.build("The api path does not exist.");
        // 获取状态码
        int httpStatusCode = errorReturn.getErrorType().getHttpStatusCode();
        // 设置状态码
        servletResponse.setStatus(httpStatusCode);
        return buildExceptionResponse(errorReturn, errorTip);
    }

    /**
     * 自定义服务异常
     *
     * @param e 异常类型 {@link ServiceException}
     * @return {@link ExceptionResponse}
     */
    @ExceptionHandler(value = ServiceException.class)
    public ExceptionResponse handle(ServiceException e) {
        // 错误返回信息
        ErrorReturn errorReturn = e.getErrorReturn();
        // 获取ErrorTip
        ErrorTip errorTip = e.getErrorTip();
        // 获取状态码
        int httpStatusCode = errorReturn.getErrorType().getHttpStatusCode();
        // 设置状态码
        servletResponse.setStatus(httpStatusCode);
        return buildExceptionResponse(errorReturn, errorTip);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ExceptionResponse handle(ConstraintViolationException e) {
        String localizedMessage = e.getLocalizedMessage();
        Throwable cause = e.getCause();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        return null;
    }

    /**
     * 处理 application/json 类型请求的参数校验失败导致的异常
     *
     * @param e 校验异常
     * @return {@link ExceptionResponse}<{@link ?}>
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ExceptionResponse handle(MethodArgumentNotValidException e) {
        String fieldJsonName = this.getFieldJsonName(e);
        String validationMessage = this.getValidationMessage(e);

        ErrorFieldTip errorFieldTip = ErrorFieldTip.build(fieldJsonName, validationMessage);
        int httpStatusCode = HttpStatus.BAD_REQUEST.value();
        String errorCode = "HD.1074002001";
        String errorMsg = "参数校验";
        return buildExceptionResponse(httpStatusCode, errorCode, errorMsg, errorFieldTip);
    }

    /**
     * 获取注解校验消息
     *
     * @param e e
     * @return {@link String}
     */
    private String getValidationMessage(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        assert fieldError != null;
        return fieldError.getDefaultMessage();
    }

    /**
     * 获取字段json名称
     *
     * @param e e
     * @return {@link String}
     */
    private String getFieldJsonName(MethodArgumentNotValidException e) {
        String fieldName = "unknown-field";
        try {
            BindingResult bindingResult = e.getBindingResult();
            FieldError fieldError = bindingResult.getFieldError();
            assert fieldError != null;
            fieldName = fieldError.getField();

            MethodParameter methodParameter = e.getParameter();
            Parameter parameter = methodParameter.getParameter();
            Class<?> classType = parameter.getType();
            Field field = classType.getDeclaredField(fieldName);
            JsonProperty jsonPropertyAnnotation = field.getAnnotation(JsonProperty.class);
            if (null == jsonPropertyAnnotation) {
                return fieldName;
            }
            return jsonPropertyAnnotation.value();
        } catch (Exception ex) {
            return fieldName;
        }
    }

    /**
     * 构建异常响应信息对象
     *
     * @param httpStatusCode HTTP状态码
     * @param errorCode      错误码
     * @param errorType      错误类型
     * @param tip            提示信息
     * @return {@link ExceptionResponse}
     */
    private ExceptionResponse buildExceptionResponse(int httpStatusCode, String errorCode, String errorType, ErrorTip tip) {
        ExceptionResponse response = ExceptionResponse.builder()
                // HTTP状态码
                .statusCode(httpStatusCode)
                // 设置错误码
                .errorCode(errorCode)
                // 设置错误类型
                .errorType(errorType)
                // 设置错误 提示信息
                .data(tip)
                // 设置响应时间戳
                .timestamp(ZonedDateTime.now()).build();
        return response;
    }

    /**
     * 给ExceptionResponse赋值
     *
     * @param errorReturn 错误返回信息对象 {@link ErrorReturn}
     * @return {@link ExceptionResponse}<{@link ?}>
     */
    private ExceptionResponse buildExceptionResponse(ErrorReturn errorReturn, ErrorTip tip) {
        int httpStatusCode = errorReturn.getErrorType().getHttpStatusCode();
        String errorType = errorReturn.getErrorType().getName();
        String errorCode = errorReturn.getErrorCode();
        return buildExceptionResponse(httpStatusCode, errorCode, errorType, tip);
    }

}
