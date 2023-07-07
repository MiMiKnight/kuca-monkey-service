package cn.yhm.developer.monkey.common.aspect;

import cn.yhm.developer.kuca.ecology.model.response.ExceptionResponse;
import cn.yhm.developer.monkey.common.enumeration.ErrorReturn;
import cn.yhm.developer.monkey.common.enumeration.ErrorType;
import cn.yhm.developer.monkey.common.exception.ServiceException;
import cn.yhm.developer.monkey.common.tip.ErrorFieldTip;
import cn.yhm.developer.monkey.common.tip.ErrorTip;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.time.ZonedDateTime;
import java.util.Map;
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

    private HttpServletResponse servletResponse;

    @Autowired
    public void setServletResponse(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }

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
        int httpStatusCode = errorReturn.getErrorType().getHttpStatusCode();
        servletResponse.setStatus(httpStatusCode);
        return buildExceptionResponse(errorReturn);
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
        int httpStatusCode = errorReturn.getErrorType().getHttpStatusCode();
        servletResponse.setStatus(httpStatusCode);
        return buildExceptionResponse(errorReturn);
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
        int httpStatusCode = errorReturn.getErrorType().getHttpStatusCode();
        servletResponse.setStatus(httpStatusCode);
        return buildExceptionResponse(errorReturn);
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
        int httpStatusCode = errorReturn.getErrorType().getHttpStatusCode();
        servletResponse.setStatus(httpStatusCode);
        return buildExceptionResponse(errorReturn);
    }

    /**
     * 处理表单类型请求的复杂参数校验失败导致的异常
     *
     * @param e 校验异常
     * @return {@link ExceptionResponse}
     */
    @ExceptionHandler(BindException.class)
    public ExceptionResponse handle(BindException e) {
        return null;
    }

    /**
     * 处理表单类型请求普通参数缺失导致的异常
     *
     * @param e 校验异常
     * @return {@link ExceptionResponse}
     */
    @ExceptionHandler(ServletRequestBindingException.class)
    public ExceptionResponse handle(ServletRequestBindingException e) {
        return null;
    }

    /**
     * 处理标注了 @Validated 的类的方法调用参数校验失败导致的异常
     *
     * @param e 校验异常
     * @return {@link ExceptionResponse}<{@link ?}>
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ExceptionResponse handle(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        violations.forEach(v -> {
            String message = v.getMessage();
            Class<?> rootBeanClass = v.getRootBeanClass();
            Object invalidValue = v.getInvalidValue();
            ConstraintDescriptor<?> descriptor = v.getConstraintDescriptor();
            String messageTemplate = v.getMessageTemplate();
            Path propertyPath = v.getPropertyPath();
            Object[] executableParameters = v.getExecutableParameters();
            Object executableReturnValue = v.getExecutableReturnValue();
            Object leafBean = v.getLeafBean();
            log.info("test");
        });
        // 错误返回信息
        ErrorReturn defaultException = ErrorReturn.DEFAULT_EXCEPTION;
        return buildExceptionResponse(defaultException, defaultException.getTip());
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
     * 包装Tip字段
     *
     * @param tip 提示字段
     * @return {@link Object}
     */
    private <T> Object packTip(T tip) {
        if (tip instanceof String) {
            return ErrorTip.build((String) tip);
        }
        if (tip instanceof ErrorTip) {
            return tip;
        }
        if (tip instanceof Map) {
            return tip;
        }
        log.warn("Tip field data type is illegal.");
        return tip;
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
    private <T> ExceptionResponse buildExceptionResponse(int httpStatusCode, String errorCode, String errorType, T tip) {
        return ExceptionResponse.builder()
                // HTTP状态码
                .statusCode(httpStatusCode)
                // 设置错误码
                .errorCode(errorCode)
                // 设置错误类型
                .errorType(errorType)
                // 设置错误 提示信息
                .data(packTip(tip))
                // 设置响应时间戳
                .timestamp(ZonedDateTime.now())
                .build();
    }

    /**
     * 构建异常响应信息对象
     *
     * @param errorReturn 错误返回信息对象 {@link ErrorReturn}
     * @param tip         提示信息
     * @return {@link ExceptionResponse}<{@link ?}>
     */
    private <T> ExceptionResponse buildExceptionResponse(ErrorReturn errorReturn, T tip) {
        String errorCode = errorReturn.getErrorCode();
        ErrorType errorType = errorReturn.getErrorType();
        int httpStatusCode = errorType.getHttpStatusCode();
        String errorTypeName = errorType.getName();
        return buildExceptionResponse(httpStatusCode, errorCode, errorTypeName, tip);
    }

    /**
     * 给ExceptionResponse赋值
     *
     * @param errorReturn 错误返回信息对象 {@link ErrorReturn}
     * @return {@link ExceptionResponse}<{@link ?}>
     */
    private ExceptionResponse buildExceptionResponse(ErrorReturn errorReturn) {
        return buildExceptionResponse(errorReturn, errorReturn.getTip());
    }

}
