package com.epamtask.aspect;

import com.epamtask.aspect.annotation.Authenticated;
import com.epamtask.security.AuthContextHolder;
import com.epamtask.service.AuthenticationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthenticationAspect {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationAspect.class);
    private final AuthenticationService authenticationService;

    public AuthenticationAspect(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Around("@annotation(authenticated)")
    public Object authenticate(ProceedingJoinPoint joinPoint, Authenticated authenticated) throws Throwable {
        String username;
        String password;
        try {
            username = AuthContextHolder.getUsername();
            password = AuthContextHolder.getPassword();
        } catch (Exception e) {
            logger.error("Error retrieving credentials: {}", e.getMessage());
            throw new SecurityException("Access denied: missing credentials.");
        }


        if (!authenticationService.authenticate(username, password)) {
            logger.error("AUTH: Authentication failed for user: {}", username);
            throw new SecurityException("Access denied: user is not authenticated.");
        }

        logger.info("AUTH: Authentication successful for user: {}", username);
        return joinPoint.proceed();
    }


    public boolean trySilentAuth(String username, String password) {
        return authenticationService.authenticate(username, password);
    }
}