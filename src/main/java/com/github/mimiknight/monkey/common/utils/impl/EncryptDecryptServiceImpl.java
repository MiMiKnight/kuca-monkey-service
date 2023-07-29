package com.github.mimiknight.monkey.common.utils.impl;

import com.github.mimiknight.monkey.common.utils.standard.EncryptDecryptService;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 加密解密工具实现类
 *
 * @author Jack
 * @since 2023-07-17 12:40:41
 */
@Component
public class EncryptDecryptServiceImpl implements EncryptDecryptService {
    private StringEncryptor stringEncryptor;

    @Autowired
    public void setStringEncryptor(StringEncryptor stringEncryptor) {
        this.stringEncryptor = stringEncryptor;
    }

    @Override
    public String encrypt(String plaintext) {
        return stringEncryptor.encrypt(plaintext);
    }

    @Override
    public String decrypt(String ciphertext) {
        return stringEncryptor.decrypt(ciphertext);
    }
}
