package com.github.mimiknight.monkey.common.enumeration;

/**
 * 错误代码
 * 错误码
 * <p>
 * 格式：
 * <p>
 * AAA.BBB-CCC-DDDD
 * <p>
 * AAA：项目标识
 * <p>
 * BBB：
 * <p>
 * CCC：错误类型
 * <p>
 * DDDD：错误码
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-02 15:42:51
 */
public interface ErrorCode {

    //************************************系统异常*************************************//
    String SYSTEM_CODE_001 = "107.400-0001";
    String SYSTEM_CODE_002 = "107.400-0002";
    String SYSTEM_CODE_003 = "107.400-0003";

    //********************************注解参数校验异常***********************************//
    String ANNOTATION_VALID_CODE_001 = "107.401-0001";
    String ANNOTATION_VALID_CODE_002 = "107.401-0002";
    String ANNOTATION_VALID_CODE_003 = "107.401-0003";

    //********************************手动参数校验异常***********************************//
    String MANUAL_VALID_CODE_001 = "HD.40504001";
    
    //************************************业务异常*************************************//
    String SERVICE_CODE_001 = "HD.40506001";
    String SERVICE_CODE_002 = "HD.40506002";
    String SERVICE_CODE_003 = "HD.40506003";

    //************************************调用第三方接口异常******************************//

    //************************************SQL异常**************************************//


}
