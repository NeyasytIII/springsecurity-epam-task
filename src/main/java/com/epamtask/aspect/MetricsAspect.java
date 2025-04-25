package com.epamtask.aspect;

import com.epamtask.aspect.annotation.MeasureApi;
import com.epamtask.aspect.annotation.MeasureDb;
import com.epamtask.service.metrics.ApiMetricsService;
import com.epamtask.service.metrics.DatabaseMetricsService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Aspect
@Component
@Profile("prod")
public class MetricsAspect {

    private final ApiMetricsService api;
    private final DatabaseMetricsService db;

    public MetricsAspect(ApiMetricsService api, DatabaseMetricsService db) {
        this.api = api;
        this.db = db;
    }

    @Around("@annotation(measure)")
    public Object aroundApi(ProceedingJoinPoint p, MeasureApi measure) throws Throwable {
        long start = System.nanoTime();
        Object result = p.proceed();
        long duration = System.nanoTime() - start;

        int status = 200;
        long payload = 0;
        if (result instanceof ResponseEntity<?> resp) {
            status = resp.getStatusCode().value();
            Object body = resp.getBody();
            payload = body != null ? body.toString().getBytes(StandardCharsets.UTF_8).length : 0;
        }

        api.track(measure.endpoint(), measure.method(), status, payload, () -> {});
        return result;
    }

    @Around("@annotation(measure)")
    public Object aroundDb(ProceedingJoinPoint p, MeasureDb measure) throws Throwable {
        return db.trackCallable(measure.table(), measure.operation(), () -> {
            try {
                return p.proceed();
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }
}