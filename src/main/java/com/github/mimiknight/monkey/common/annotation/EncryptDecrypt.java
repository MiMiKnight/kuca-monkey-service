package com.github.mimiknight.monkey.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加密解密拦截注解
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-07-27 20:25:43
 */
@Documented
@Target(value = {ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EncryptDecrypt {
}
