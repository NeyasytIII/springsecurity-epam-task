package com.epamtask.service.metrics;

import com.epamtask.aspect.annotation.Loggable;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ApiMetricsService {

    private final MeterRegistry reg;
    private final AtomicInteger active = new AtomicInteger(0);
    private final Counter slowGlobal;
    private final Map<String, Counter> reqCounter = new ConcurrentHashMap<>();
    private final Map<String, Counter> errCounter = new ConcurrentHashMap<>();
    private final Map<String, Timer> reqTimer = new ConcurrentHashMap<>();
    private final Map<String, DistributionSummary> payloadSummary = new ConcurrentHashMap<>();
    private static final long SLOW_NS = TimeUnit.SECONDS.toNanos(2);

    public ApiMetricsService(MeterRegistry reg) {
        this.reg = reg;
        Gauge.builder("custom.api.active.requests", active, AtomicInteger::get).register(reg);
        this.slowGlobal = Counter.builder("custom.api.slow.count").register(reg);
    }
    @Loggable
    public void track(String ep, String method, int status, long bytes, Runnable task) {
        String key = ep + "|" + method + "|" + status;
        reqCounter.computeIfAbsent(key, k -> Counter.builder("custom.api.request.count").tags("endpoint", ep, "method", method, "status", String.valueOf(status)).register(reg)).increment();
        if (status >= 400) {
            String cls = status >= 500 ? "5xx" : "4xx";
            errCounter.computeIfAbsent(ep + "|" + cls, k -> Counter.builder("custom.api.error.count").tags("endpoint", ep, "class", cls).register(reg)).increment();
        }
        payloadSummary.computeIfAbsent(ep + "|" + method, k -> DistributionSummary.builder("custom.api.payload.size").tags("endpoint", ep, "method", method).publishPercentileHistogram().register(reg)).record(bytes);
        Timer timer = reqTimer.computeIfAbsent(key, k -> Timer.builder("custom.api.request.time").tags("endpoint", ep, "method", method, "status", String.valueOf(status)).publishPercentileHistogram().register(reg));
        active.incrementAndGet();
        long st = System.nanoTime();
        try { task.run(); } finally {
            long dur = System.nanoTime() - st;
            timer.record(dur, TimeUnit.NANOSECONDS);
            if (dur > SLOW_NS) slowGlobal.increment();
            active.decrementAndGet();
        }
    }
    @Loggable
    public <T> T trackCallable(String ep, String method, int status, long bytes, java.util.concurrent.Callable<T> call) throws Exception {
        final Object[] res = new Object[1];
        final Exception[] ex = new Exception[1];

        Runnable safeTask = () -> {
            try {
                res[0] = call.call();
            } catch (Exception e) {
                ex[0] = e;
            }
        };
        track(ep, method, status, bytes, safeTask);

        if (ex[0] != null) {
            throw ex[0];
        }

        @SuppressWarnings("unchecked")
        T r = (T) res[0];
        return r;
    }
}