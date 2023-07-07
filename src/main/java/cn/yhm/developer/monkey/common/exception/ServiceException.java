package cn.yhm.developer.monkey.common.exception;

import cn.yhm.developer.monkey.common.enumeration.ErrorReturn;
import lombok.Getter;

/**
 * 自定义业务异常
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-10 19:04:16
 */
@Getter
public class ServiceException extends RuntimeException {

    /**
     * 错误返回
     */
    private final ErrorReturn errorReturn;

    /**
     * 自定义服务异常 构造方法
     *
     * @param errorReturn 错误返回
     */
    public ServiceException(ErrorReturn errorReturn) {
        this.errorReturn = errorReturn;
    }

    /**
     * 自定义服务异常 构造方法
     *
     * @param errorReturn 错误返回
     * @param cause       原因
     */
    public ServiceException(ErrorReturn errorReturn, Throwable cause) {
        super(cause);
        this.errorReturn = errorReturn;
    }

}
