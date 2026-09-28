package com.taskflow.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j // Аннотация Lombok для создания логгера
@Aspect // Говорим Spring, что это аспект
@Component // Делаем его бином, чтобы Spring его увидел
public class LoggingAspect {

    /**
     * Перехватываем выполнение ВСЕХ методов во ВСЕХ классах пакета com.taskflow.service
     */
    @Around("execution(* com.taskflow.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        // Получаем имя класса и метода для красивого лога
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("▶️ START: {}.{}()", className, methodName);

        // Вызываем сам целевой метод (тот, который мы перехватили)
        Object result = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;
        log.info("✅ FINISH: {}.{}() executed in {} ms", className, methodName, executionTime);

        return result;
    }
}