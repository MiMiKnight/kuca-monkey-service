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

    private final ErrorReturn errorReturn;


    public ServiceException(ErrorReturn errorReturn) {
        this.errorReturn = errorReturn;
    }

}
