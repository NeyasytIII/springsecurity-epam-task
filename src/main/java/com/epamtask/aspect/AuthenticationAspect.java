package com.epamtask.aspect;

import com.epamtask.aspect.annotation.Authenticated;
import com.epamtask.security.AuthSessionStore;
import com.epamtask.security.AuthTokenFilter;
import com.epamtask.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthenticationAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationAspect.class);
    private final AuthenticationService authenticationService;
    private final AuthSessionStore sessionStore;

    public AuthenticationAspect(AuthenticationService authenticationService, AuthSessionStore sessionStore) {
        this.authenticationService = authenticationService;
        this.sessionStore = sessionStore;
    }

    @Around("@annotation(authenticated)")
    public Object authenticate(ProceedingJoinPoint joinPoint, Authenticated authenticated) throws Throwable {
        HttpServletRequest request = getCurrentHttpRequest();
        String token = request.getHeader("X-Auth-Token");

        if (token == null || token.isBlank()) {
            logger.error("Missing X-Auth-Token header");
            throw new SecurityException("Access denied: missing token.");
        }

        AuthSessionStore.Credentials credentials = sessionStore.getCredentials(token);
        String username = AuthTokenFilter.getUsername();
        String password = AuthTokenFilter.getPassword();

        if (!authenticationService.authenticate(username, password)) {
            logger.error("AUTH: Authentication failed for user: {}", username);
            throw new SecurityException("Access denied: user is not authenticated.");
        }

        logger.info("AUTH: Authentication successful for user: {}", username);
        return joinPoint.proceed();
    }

    private HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes attrs) {
            return attrs.getRequest();
        }
        throw new IllegalStateException("No current HTTP request");
    }
}