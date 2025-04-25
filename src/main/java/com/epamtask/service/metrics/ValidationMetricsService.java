package com.epamtask.service.metrics;

import com.epamtask.aspect.annotation.Loggable;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
public class ValidationMetricsService {

    private final MeterRegistry reg;

    public ValidationMetricsService(MeterRegistry reg) {
        this.reg = reg;
    }
    @Loggable
    public void error(String dto) {
        Counter.builder("custom.validation.error").tag("dto", dto).register(reg).increment();
    }
}