package com.epamtask.metrics;

import com.epamtask.service.metrics.ApiMetricsService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ApiMetricsServiceTest {

    private MeterRegistry meterRegistry;
    private ApiMetricsService metricsService;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metricsService = new ApiMetricsService(meterRegistry);
    }

    @Test
    void shouldTrackRequestAndUpdateMetrics() {
        metricsService.track("/test", "GET", 200, 123L, () -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        double count = meterRegistry.get("custom.api.request.count")
                .tag("endpoint", "/test")
                .tag("method", "GET")
                .tag("status", "200")
                .counter().count();
        assertEquals(1.0, count);

        double payload = meterRegistry.get("custom.api.payload.size")
                .tag("endpoint", "/test")
                .tag("method", "GET")
                .summary().count();
        assertEquals(1.0, payload);

        double active = meterRegistry.get("custom.api.active.requests")
                .gauge().value();
        assertEquals(0.0, active);
    }

    @Test
    void shouldTrackCallableAndReturnValue() throws Exception {
        String result = metricsService.trackCallable("/test", "POST", 200, 256L, () -> "OK");
        assertEquals("OK", result);

        double count = meterRegistry.get("custom.api.request.count")
                .tag("endpoint", "/test")
                .tag("method", "POST")
                .tag("status", "200")
                .counter().count();
        assertEquals(1.0, count);
    }

    @Test
    void shouldIncrementSlowCountIfExecutionTakesTooLong() {
        metricsService.track("/slow", "GET", 200, 0L, () -> {
            try {
                Thread.sleep(2100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        double slowCount = meterRegistry.get("custom.api.slow.count").counter().count();
        assertEquals(1.0, slowCount);
    }
}
