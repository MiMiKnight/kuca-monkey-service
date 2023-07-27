package com.github.mimiknight.monkey.common.exception;

import com.github.mimiknight.monkey.common.enumeration.ErrorReturn;
import com.github.mimiknight.monkey.common.tip.ErrorTip;
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
     * 错误提示
     */
    private final transient ErrorTip errorTip;

    /**
     * 服务异常 构造方法
     *
     * @param errorReturn 错误返回
     */
    public ServiceException(ErrorReturn errorReturn, ErrorTip errorTip) {
        super(errorTip.getTip());
        this.errorReturn = errorReturn;
        this.errorTip = errorTip;
    }

    /**
     * 服务异常
     *
     * @param errorReturn 错误返回
     * @param message     异常消息
     */
    public ServiceException(ErrorReturn errorReturn, String message) {
        this(errorReturn, ErrorTip.build(message));
    }


    /**
     * 服务异常
     *
     * @param errorReturn 错误返回
     */
    public ServiceException(ErrorReturn errorReturn) {
        this(errorReturn, ErrorTip.build(errorReturn.getMessage()));
    }

    /**
     * 服务异常  构造方法
     *
     * @param errorReturn 错误返回
     * @param cause       原因
     */
    public ServiceException(ErrorReturn errorReturn, ErrorTip errorTip, Throwable cause) {
        super(errorTip.getTip(), cause);
        this.errorReturn = errorReturn;
        this.errorTip = errorTip;
    }

    /**
     * 服务异常
     *
     * @param errorReturn 错误返回
     * @param message     消息
     * @param cause       原因
     */
    public ServiceException(ErrorReturn errorReturn, String message, Throwable cause) {
        this(errorReturn, ErrorTip.build(message), cause);
    }

    /**
     * 服务异常
     *
     * @param errorReturn 错误返回
     * @param cause       原因
     */
    public ServiceException(ErrorReturn errorReturn, Throwable cause) {
        this(errorReturn, ErrorTip.build(errorReturn.getMessage()), cause);
    }

}
