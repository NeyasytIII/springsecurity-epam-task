package com.epamtask.metrics;

import com.epamtask.service.metrics.DatabaseMetricsService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseMetricsServiceTest {

    private MeterRegistry registry;
    private DatabaseMetricsService service;

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
        service = new DatabaseMetricsService(registry);
    }

    @Test
    void trackShouldRecordMetrics() {
        service.track("trainer", "select", () -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {}
        });

        double count = registry.get("custom.db.request.count")
                .tag("table", "trainer")
                .tag("operation", "select")
                .counter().count();

        Timer timer = registry.get("custom.db.request.time")
                .tag("table", "trainer")
                .tag("operation", "select")
                .timer();

        assertEquals(1.0, count);
        assertEquals(1, timer.count());
        assertTrue(timer.totalTime(TimeUnit.NANOSECONDS) > 0);
    }

    @Test
    void trackCallableShouldReturnValueAndRecord() throws Exception {
        String result = service.trackCallable("trainee", "insert", () -> "done");

        assertEquals("done", result);

        double count = registry.get("custom.db.request.count")
                .tag("table", "trainee")
                .tag("operation", "insert")
                .counter().count();

        assertEquals(1.0, count);
    }

    @Test
    void shouldUpdateTotalCountersAndGauges() {
        service.track("training_type", "select", () -> {});

        long totalCount = service.getTotalCount();
        long tableCount = service.getTableCount("training_type");

        assertEquals(1, totalCount);
        assertEquals(1, tableCount);
    }

    @Test
    void shouldCalculateAveragesCorrectly() {
        service.track("trainer", "select", () -> {
            try {
                Thread.sleep(5);
            } catch (InterruptedException ignored) {}
        });

        service.track("trainer", "select", () -> {
            try {
                Thread.sleep(5);
            } catch (InterruptedException ignored) {}
        });

        double avg = service.getTableAvgTime("trainer", TimeUnit.MILLISECONDS);
        assertTrue(avg >= 5.0 && avg <= 20.0);
    }
}