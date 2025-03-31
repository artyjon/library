package com.romsa.library.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.romsa.library.service.*.*(..))")
    public void logServiceMethods(JoinPoint joinPoint) {
        logger.info("Executing: {}", joinPoint.getSignature().getName());
    }


    @Before("execution(* com.romsa.library.controller.*.*(..))")
    public void logControllerBefore(JoinPoint joinPoint) {
        logger.info("Вызов метода: {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* com.romsa.library.controller.*.*(..))", returning = "result")
    public void logControllerAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Метод {} завершился успешно", joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "execution(* com.romsa.library.controller.*.*(..))", throwing = "exception")
    public void logControllerAfterThrowing(JoinPoint joinPoint, Exception exception) {
        logger.error("Ошибка в методе {}: {}", joinPoint.getSignature().getName(), exception.getMessage());
    }
}
