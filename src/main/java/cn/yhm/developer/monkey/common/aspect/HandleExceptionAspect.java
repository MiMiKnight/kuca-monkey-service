package cn.yhm.developer.monkey.common.aspect;

import cn.yhm.developer.kuca.ecology.model.response.ExceptionResponse;
import cn.yhm.developer.monkey.common.enumeration.ErrorReturn;
import cn.yhm.developer.monkey.common.tip.ErrorTip;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.time.ZonedDateTime;
import java.util.HashMap;

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

    /**
     * 默认异常处理
     *
     * @param e               异常类型 {@link Exception}
     * @param servletResponse Servlet响应 {@link  HttpServletResponse}
     * @return {@link ExceptionResponse}<{@link ?}>
     */
    @ExceptionHandler(value = Exception.class)
    public ExceptionResponse<?> handle(Exception e, HttpServletResponse servletResponse) {
        // 错误返回信息
        ErrorReturn defaultException = ErrorReturn.DEFAULT_EXCEPTION;
        // 设置响应的HTTP状态码
        servletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ExceptionResponse<?> exceptionResponse = buildExceptionResponse(defaultException, defaultException.getTip());
        return buildExceptionResponse(defaultException, defaultException.getTip());
    }


    /**
     * 给ExceptionResponse赋值
     *
     * @param errorReturn 错误返回信息对象 {@link ErrorReturn}
     * @return {@link ExceptionResponse}<{@link ?}>
     */
    private ExceptionResponse<?> buildExceptionResponse(ErrorReturn errorReturn) {
        return buildExceptionResponse(errorReturn, errorReturn.getTip());
    }

    /**
     * 包装Tip字段
     *
     * @param tip 提示字段
     * @return {@link Object}
     */
    private Object packTip(Object tip) {
        if (tip instanceof String) {
            return ErrorTip.build((String) tip);
        }
        if (tip instanceof ErrorTip) {
            return tip;
        }
        if (tip instanceof HashMap) {
            return tip;
        }
        log.warn("Tip field data type is illegal.");
        return tip;
    }

    /**
     * 给ExceptionResponse赋值
     *
     * @param errorReturn 错误返回信息对象 {@link ErrorReturn}
     * @param tip         提示信息
     * @return {@link ExceptionResponse}<{@link ?}>
     */
    private <T> ExceptionResponse<?> buildExceptionResponse(ErrorReturn errorReturn, T tip) {
        ExceptionResponse<Object> exceptionResponse = ExceptionResponse.builder()
                // 设置错误码
                .errorCode(errorReturn.getErrorCode())
                // HTTP状态码
                .httpStatus(errorReturn.getErrorType().getHttpStatusCode())
                // 设置错误类型
                .errorType(errorReturn.getErrorType().getName())
                // 设置错误 提示信息
                .data(packTip(tip))
                // 设置响应时间戳
                .timestamp(ZonedDateTime.now())
                .build();
        // 异常响应日志打印
        // 清除当前请求线程中的跟踪ID
        MDC.clear();
        return exceptionResponse;
    }


}
