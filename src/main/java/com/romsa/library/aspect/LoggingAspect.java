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

    // Инициализация логгера для записи событий
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Логирование вызовов методов сервисного слоя
     * Перехватывает выполнение любого метода в пакете service
     */
    @Before("execution(* com.romsa.library.service.*.*(..))")
    public void logServiceMethods(JoinPoint joinPoint) {
        logger.info("Executing: {}", joinPoint.getSignature().getName());
    }

    /**
     * Логирование начала выполнения методов контроллера
     * Срабатывает перед вызовом любого метода в пакете controller
     */
    @Before("execution(* com.romsa.library.controller.*.*(..))")
    public void logControllerBefore(JoinPoint joinPoint) {
        logger.info("Вызов метода: {}", joinPoint.getSignature().getName());
    }

    /**
     * Логирование успешного завершения методов контроллера
     * Срабатывает после успешного выполнения метода
     * @param result - возвращаемое значение метода
     */
    @AfterReturning(
            pointcut = "execution(* com.romsa.library.controller.*.*(..))",
            returning = "result"
    )
    public void logControllerAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Метод {} завершился успешно", joinPoint.getSignature().getName());
    }

    /**
     * Логирование ошибок в методах контроллера
     * Срабатывает при ВОЗНИКНОВЕНИИ ИСКЛЮЧЕНИЯ в методе
     * @param exception - пойманное исключение
     */
    @AfterThrowing(
            pointcut = "execution(* com.romsa.library.controller.*.*(..))",
            throwing = "exception"
    )
    public void logControllerAfterThrowing(JoinPoint joinPoint, Exception exception) {
        logger.error("Ошибка в методе {}: {}",
                joinPoint.getSignature().getName(),
                exception.getMessage()
        );
    }
}