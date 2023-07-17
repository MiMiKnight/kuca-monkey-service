package cn.yhm.developer.monkey.encrypt;


import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 加密解密工具接口
 *
 * @author Jack
 * @since 2023-07-17 12:41:05
 */
@Slf4j
@SpringBootTest
public class EncryptDecryptUtilTest {
    private StringEncryptor stringEncryptor;

    @Autowired
    public void setStringEncryptor(StringEncryptor stringEncryptor) {
        this.stringEncryptor = stringEncryptor;
    }

    @Test
    public void encrypt() {
        String plaintext = "root";
        String ciphertext = stringEncryptor.encrypt(plaintext);
        log.info("ciphertext = {}", ciphertext);
    }

    @Test
    public void decrypt() {
        String ciphertext = "iEXyvT7bYi0AZsRjwSV7Dw==";
        String plaintext = stringEncryptor.decrypt(ciphertext);
        log.info("plaintext = {}", plaintext);
    }

}
