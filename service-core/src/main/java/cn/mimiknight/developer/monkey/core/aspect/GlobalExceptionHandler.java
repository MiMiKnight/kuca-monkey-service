package cn.mimiknight.developer.monkey.core.aspect;


import cn.mimiknight.developer.kuca.spring.appeasy.aspect.KucaGlobalExceptionHandlerAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * global exception handler
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @date 2024-09-22 19:44:57
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends KucaGlobalExceptionHandlerAdvice {

}
