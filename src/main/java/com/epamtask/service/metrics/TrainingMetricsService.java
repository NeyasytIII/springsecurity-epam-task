package com.epamtask.service.metrics;

import com.epamtask.aspect.annotation.Loggable;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
public class TrainingMetricsService {

    private final MeterRegistry reg;

    public TrainingMetricsService(MeterRegistry reg) {
        this.reg = reg;
    }
    @Loggable
    public void created(String type, String trainer) {
        Counter.builder("custom.training.created").tags("type", type, "trainer", trainer).register(reg).increment();
    }
    @Loggable
    public void duration(String type, long min) {
        DistributionSummary.builder("custom.training.duration").tag("type", type).publishPercentileHistogram().register(reg).record(min);
    }
    @Loggable
    public void cascade(long sub) {
        Counter.builder("custom.training.cascade.del").register(reg).increment();
        DistributionSummary.builder("custom.training.cascade.entities").register(reg).record(sub);
    }
}