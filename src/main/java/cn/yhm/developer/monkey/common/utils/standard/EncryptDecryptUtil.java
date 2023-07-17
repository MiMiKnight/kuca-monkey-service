package cn.yhm.developer.monkey.common.utils.standard;


/**
 * 加密解密工具接口
 *
 * @author Jack
 * @since 2023-07-17 12:41:05
 */
public interface EncryptDecryptUtil {
    /**
     * 加密
     *
     * @param plaintext 明文
     * @return {@link String}
     */
    String encrypt(String plaintext);

    /**
     * 解密
     *
     * @param ciphertext 密文
     * @return {@link String}
     */
    String decrypt(String ciphertext);

}
