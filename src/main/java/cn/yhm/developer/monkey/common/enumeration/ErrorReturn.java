package cn.yhm.developer.monkey.common.enumeration;


import cn.yhm.developer.monkey.common.tip.ErrorFieldTip;
import cn.yhm.developer.monkey.common.tip.ErrorTip;
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
    DEFAULT_EXCEPTION(ErrorCode.SYSTEM_CODE_001, ErrorType.DEFAULT_EXCEPTION, ErrorTip.build("系统发生未知错误")),
    RESOURCE_NOT_FOUND(ErrorCode.SYSTEM_CODE_002, ErrorType.RESOURCE_NOT_FOUND, ErrorTip.build("接口路径不存在")),
    METHOD_NOT_ALLOWED(ErrorCode.SYSTEM_CODE_003, ErrorType.METHOD_NOT_ALLOWED, ErrorTip.build("请求方法错误")),

    //********************************注解参数校验异常***********************************//

    //********************************手动参数校验异常***********************************//
    PARAMETER_MANUAL_VALID_ERROR_001(ErrorCode.MANUAL_VALID_CODE_001, ErrorType.PARAMETER_VALID_FAILED,
            ErrorFieldTip.build("page_size", "page_size大于等于1小于等于100")),

    //************************************业务异常*************************************//
    SERVICE_ERROR_001(ErrorCode.SERVICE_CODE_001, ErrorType.SERVICE_EXCEPTION, ErrorTip.build("用户未成年"));

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
     */
    private final ErrorTip errorTip;


    /**
     * @param errorCode 错误码
     * @param errorType 错误类型
     * @param errorTip  错误提示信息
     */
    ErrorReturn(String errorCode, ErrorType errorType, ErrorTip errorTip) {
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.errorTip = errorTip;
    }

}
