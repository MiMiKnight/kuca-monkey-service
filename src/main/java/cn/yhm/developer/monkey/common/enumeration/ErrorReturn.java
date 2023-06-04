package cn.yhm.developer.monkey.common.enumeration;


import cn.yhm.developer.monkey.common.tip.ErrorFieldTip;
import cn.yhm.developer.monkey.common.tip.ErrorTip;

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
public enum ErrorReturn {

    //************************************系统异常*************************************//
    DEFAULT_EXCEPTION(ErrorCode.SYSTEM_CODE_001, ErrorType.DEFAULT_EXCEPTION, "系统发生未知错误"),
    RESOURCE_NOT_FOUND(ErrorCode.SYSTEM_CODE_002, ErrorType.RESOURCE_NOT_FOUND, "接口路径不存在"),
    METHOD_NOT_ALLOWED(ErrorCode.SYSTEM_CODE_003, ErrorType.METHOD_NOT_ALLOWED, "请求方法错误"),

    //********************************注解参数校验异常***********************************//
    PARAMETER_ANNOTATION_VALID_ERROR_001(ErrorCode.ANNOTATION_VALID_CODE_001, ErrorType.PARAMETER_VALID_FAILED,
            ErrorFieldTip.build("audit_result", "审核结果参数数值不合法")),
    PARAMETER_ANNOTATION_VALID_ERROR_002(ErrorCode.ANNOTATION_VALID_CODE_002, ErrorType.PARAMETER_VALID_FAILED,
            ErrorFieldTip.build("audit_time", "审核时间日期不合法")),
    PARAMETER_ANNOTATION_VALID_ERROR_003(ErrorCode.ANNOTATION_VALID_CODE_003, ErrorType.PARAMETER_VALID_FAILED,
            ErrorFieldTip.build("id", "数值不合法")),

    //********************************手动参数校验异常***********************************//
    PARAMETER_MANUAL_VALID_ERROR_001(ErrorCode.MANUAL_VALID_CODE_001, ErrorType.PARAMETER_VALID_FAILED,
            ErrorFieldTip.build("page_size", "page_size大于等于1小于等于100")),

    //************************************业务异常*************************************//
    SERVICE_ERROR_001(ErrorCode.SERVICE_CODE_001, ErrorType.SERVICE_EXCEPTION, "用户未成年");

    /**
     * 错误码
     */
    private final String errorCode;

    /**
     * 错误类型
     */
    private final ErrorType errorType;

    /**
     * 错误提示信息
     * <p>
     * 仅限传入以下类型参数，项目启动时会扫描检测，检测不符合要求则项目启动失败并报错给出提示信息
     * <p>
     * 错误提示类：{@link ErrorTip}
     * <p>
     * 错误字段提示类：{@link ErrorFieldTip}
     * <p>
     * 字符串：{@link String}
     * <p>
     * 哈希Map：{@link java.util.HashMap}
     */
    private final Object tip;


    /**
     * @param errorCode 错误码
     * @param errorType 错误类型
     * @param tip       错误提示信息
     */
    ErrorReturn(String errorCode, ErrorType errorType, Object tip) {
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.tip = tip;
    }

    /**
     * @param errorCode 错误码
     * @param errorType 错误类型
     */
    ErrorReturn(String errorCode, ErrorType errorType) {
        this(errorCode, errorType, "");
    }

    /**
     * 默认错误类型：ErrorType.SERVICE_EXCEPTION
     *
     * @param errorCode 错误码
     * @param message   错误提示消息
     */
    ErrorReturn(String errorCode, Object message) {
        this(errorCode, ErrorType.SERVICE_EXCEPTION, message);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public Object getTip() {
        return tip;
    }
}
