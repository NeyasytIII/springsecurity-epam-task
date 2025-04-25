package com.epamtask.metrics;

import com.epamtask.service.metrics.AuthenticationMetricsService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthenticationMetricsServiceTest {

    private MeterRegistry registry;
    private AuthenticationMetricsService service;

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
        service = new AuthenticationMetricsService(registry);
    }

    @Test
    void shouldIncrementSuccess() {
        service.success();
        double count = registry.get("custom.auth.login.success").counter().count();
        assertEquals(1.0, count);
    }

    @Test
    void shouldIncrementFail() {
        service.fail("user");
        double count = registry.get("custom.auth.login.failure").counter().count();
        double repeat = registry.get("custom.auth.repeat.failure").summary().count();
        assertEquals(1.0, count);
        assertEquals(1.0, repeat);
    }

    @Test
    void shouldIncrementLock() {
        service.lock("user1");
        double lockCount = registry.get("custom.auth.login.lock").counter().count();
        double userTagCount = registry.get("custom.auth.lock.user").tag("user", "user1").counter().count();
        assertEquals(1.0, lockCount);
        assertEquals(1.0, userTagCount);
    }

    @Test
    void shouldRecordTimed() throws Exception {
        String result = service.timed(() -> "OK");
        assertEquals("OK", result);
        double count = registry.get("custom.auth.login.time").timer().count();
        assertEquals(1.0, count);
    }
}