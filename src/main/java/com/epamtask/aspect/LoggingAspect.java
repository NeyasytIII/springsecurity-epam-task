package com.epamtask.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("@annotation(com.epamtask.aspect.annotation.Loggable)")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        logger.info("Calling method: {}", methodName);

        if (args != null && args.length > 0) {
            for (Object arg : args) {
                logger.debug("Argument: {}", arg);
            }
        }

        long startTime = System.currentTimeMillis();
        Object result;

        try {
            result = joinPoint.proceed();
        } catch (Exception ex) {
            logger.error("Error in method: {}. Message: {}", methodName, ex.getMessage(), ex);
            throw ex;
        }

        long executionTime = System.currentTimeMillis() - startTime;
        logger.info("Method {} executed in {} ms", methodName, executionTime);

        logger.debug("Result: {}", result);

        return result;
    }
}