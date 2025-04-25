package com.epamtask.metrics;

import com.epamtask.service.metrics.ValidationMetricsService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationMetricsServiceTest {

    private MeterRegistry registry;
    private ValidationMetricsService service;

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
        service = new ValidationMetricsService(registry);
    }

    @Test
    void errorShouldIncrementCounter() {
        service.error("TrainerCreateRequestDto");

        Counter counter = registry.get("custom.validation.error")
                .tag("dto", "TrainerCreateRequestDto")
                .counter();

        assertEquals(1.0, counter.count());
    }
}