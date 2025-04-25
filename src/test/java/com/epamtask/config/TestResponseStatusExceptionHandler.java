package com.epamtask.config;

import com.epamtask.exception.InvalidCredentialsException;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Profile("test")
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TestResponseStatusExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String,Object>> handleRSE(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(Map.of(
                        "exception", ex.getClass().getSimpleName(),
                        "message",   ex.getReason(),
                        "timestamp", Instant.now().toString(),
                        "details",   List.of(ex.getReason())
                ));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String,Object>> handleInvalidCreds(InvalidCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "exception", ex.getClass().getSimpleName(),
                        "message",   ex.getMessage(),
                        "timestamp", Instant.now().toString(),
                        "details",   List.of(ex.getMessage())
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,Object>> handleBadArgs(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "exception", ex.getClass().getSimpleName(),
                        "message",   ex.getMessage(),
                        "timestamp", Instant.now().toString(),
                        "details",   List.of(ex.getMessage())
                ));
    }
}