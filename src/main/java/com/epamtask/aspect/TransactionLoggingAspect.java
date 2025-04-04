package com.epamtask.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import jakarta.transaction.Transactional;

@Aspect
@Component
public class TransactionLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(TransactionLoggingAspect.class);

    @Around("@annotation(transactional)")
    public Object logTransaction(ProceedingJoinPoint joinPoint, Transactional transactional) throws Throwable {
        logger.info("Transaction started: {}", joinPoint.getSignature());
        try {
            Object result = joinPoint.proceed();
            logger.info("Transaction completed successfully: {}", joinPoint.getSignature());
            return result;
        } catch (Throwable throwable) {
            logger.error("Transaction failed: {}", joinPoint.getSignature(), throwable);
            throw throwable;
        }
    }
}