package com.epamtask.metrics;

import com.epamtask.service.metrics.TrainingMetricsService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainingMetricsServiceTest {

    private MeterRegistry registry;
    private TrainingMetricsService service;

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
        service = new TrainingMetricsService(registry);
    }

    @Test
    void createdShouldIncrementCounter() {
        service.created("YOGA", "Alice");

        Counter counter = registry.get("custom.training.created")
                .tag("type", "YOGA")
                .tag("trainer", "Alice")
                .counter();

        assertEquals(1.0, counter.count());
    }

    @Test
    void durationShouldRecordValue() {
        service.duration("CARDIO", 45);

        DistributionSummary summary = registry.get("custom.training.duration")
                .tag("type", "CARDIO")
                .summary();

        assertEquals(1, summary.count());
        assertEquals(45.0, summary.totalAmount());
    }

    @Test
    void cascadeShouldRecordCounterAndSummary() {
        service.cascade(3);

        Counter counter = registry.get("custom.training.cascade.del").counter();
        DistributionSummary summary = registry.get("custom.training.cascade.entities").summary();

        assertEquals(1, counter.count());
        assertEquals(1, summary.count());
        assertEquals(3.0, summary.totalAmount());
    }
}