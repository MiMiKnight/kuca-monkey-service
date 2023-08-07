package com.github.mimiknight.monkey.common.enumeration;


import com.github.mimiknight.monkey.common.tip.ErrorFieldTip;
import com.github.mimiknight.monkey.common.tip.ErrorTip;
import lombok.Getter;

/**
 * 错误信息类
 * <p>
 * 每个错误信息类有且仅被允许使用一次，以保持错误码的唯一性（需要用户自己注意，系统无法扫描检查）
 * <p>
 * 项目启动时会扫描检测错误码，如果错误码存在重复定义则项目会启动失败并抛出异常
 * <p>
 * 格式：
 * <p>
 * HD.AAABBBB
 * <p>
 * AAA：项目编号
 * <p>
 * BBBB：异常编码
 * <p>
 * 错误提示类 {@link  ErrorTip}
 * <p>
 * 字段错误提示类 {@link  ErrorFieldTip}
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-03-10 18:46:29
 */
@Getter
public enum ErrorReturn {

    //************************************系统异常*************************************//
    /**
     * 系统发生未知错误
     */
    DEFAULT_EXCEPTION(ErrorCode.SYSTEM_CODE_001, ErrorType.DEFAULT_EXCEPTION),
    /**
     * 接口路径不存在
     */
    RESOURCE_NOT_FOUND(ErrorCode.SYSTEM_CODE_002, ErrorType.RESOURCE_NOT_FOUND),
    /**
     * 请求方法错误
     */
    METHOD_NOT_ALLOWED(ErrorCode.SYSTEM_CODE_003, ErrorType.METHOD_NOT_ALLOWED),

    //********************************注解参数校验异常***********************************//

    //********************************手动参数校验异常***********************************//
    PARAMETER_MANUAL_VALID_ERROR_001(ErrorCode.MANUAL_VALID_CODE_001, ErrorType.PARAMETER_VALID_FAILED),

    //************************************业务异常*************************************//

    /**
     * 获取锁失败
     */
    GET_LOCK_FAILED(ErrorCode.SERVICE_CODE_001, "Failed to get the lock."),

    /**
     * 文章记录不存在
     */
    ARTICLE_DO_NOT_EXIST(ErrorCode.SERVICE_CODE_002, "Article do not exist."),

    /**
     * 根据提供的文章主键集合未查到任何审核中的文章数据
     */
    NO_FIND_AUDITING_ARTICLE_BY_IDS(ErrorCode.SERVICE_CODE_003, "No find any auditing article by aricle id list.");

    /**
     * 错误码
     */
    private final String errorCode;

    /**
     * 错误类型
     */
    private final ErrorType errorType;


    /**
     * 错误提示消息
     */
    private final String message;

    /**
     * 错误返回
     *
     * @param errorCode 错误代码
     * @param errorType 错误类型
     * @param message   错误提示消息
     */
    ErrorReturn(String errorCode, ErrorType errorType, String message) {
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.message = message;
    }

    /**
     * 错误返回
     *
     * @param errorCode 错误码
     * @param errorType 错误类型
     */
    ErrorReturn(String errorCode, ErrorType errorType) {
        this(errorCode, errorType, "");
    }

    /**
     * 错误返回
     *
     * @param errorCode 错误代码
     */
    ErrorReturn(String errorCode) {
        this(errorCode, ErrorType.SERVICE_EXCEPTION);
    }

    /**
     * 错误返回
     *
     * @param errorCode 错误代码
     * @param message   错误提示消息
     */
    ErrorReturn(String errorCode, String message) {
        this(errorCode, ErrorType.SERVICE_EXCEPTION, message);
    }
}
