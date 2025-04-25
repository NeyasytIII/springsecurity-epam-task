package com.epamtask.service.metrics;

import com.epamtask.aspect.annotation.Loggable;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DatabaseMetricsService {

    private final MeterRegistry registry;
    private final Map<String, Counter> counterCache = new ConcurrentHashMap<>();
    private final Map<String, Timer>   timerCache   = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> tableCount = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> tableTime  = new ConcurrentHashMap<>();
    private final AtomicLong totalCount = new AtomicLong();
    private final AtomicLong totalTime  = new AtomicLong();

    public DatabaseMetricsService(MeterRegistry registry) {
        this.registry = registry;
        Gauge.builder("custom.db.request.total.count", totalCount, AtomicLong::get).register(registry);
        Gauge.builder("custom.db.request.total.time",  totalTime,  AtomicLong::get).register(registry);
    }
    @Loggable
    public void track(String table, String operation, Runnable task) {
        Counter c = counterCache.computeIfAbsent(table + "|" + operation,
                k -> Counter.builder("custom.db.request.count")
                        .tags("table", table, "operation", operation)
                        .register(registry));

        Timer t = timerCache.computeIfAbsent(table + "|" + operation,
                k -> Timer.builder("custom.db.request.time")
                        .tags("table", table, "operation", operation)
                        .publishPercentileHistogram()
                        .publishPercentiles(0.5, 0.9, 0.99)
                        .minimumExpectedValue(java.time.Duration.ofMillis(1))
                        .maximumExpectedValue(java.time.Duration.ofSeconds(5))
                        .register(registry));

        c.increment();
        totalCount.incrementAndGet();
        tableCount.computeIfAbsent(table, this::initCountGauge).incrementAndGet();

        long start = System.nanoTime();
        task.run();
        long dur = System.nanoTime() - start;

        t.record(dur, TimeUnit.NANOSECONDS);
        totalTime.addAndGet(dur);
        tableTime.computeIfAbsent(table, this::initTimeGauge).addAndGet(dur);
    }
    @Loggable
    public <T> T trackCallable(String table, String operation, Callable<T> call) throws Exception {
        final T[] res = (T[]) new Object[1];
        track(table, operation, () -> {
            try { res[0] = call.call(); } catch (Exception e) { throw new RuntimeException(e); }
        });
        return res[0];
    }
    @Loggable
    private AtomicLong initCountGauge(String table) {
        AtomicLong a = new AtomicLong();
        Gauge.builder("custom.db.table.request.count", a, AtomicLong::get)
                .tag("table", table).register(registry);
        return a;
    }
    @Loggable
    private AtomicLong initTimeGauge(String table) {
        AtomicLong a = new AtomicLong();
        Gauge.builder("custom.db.table.request.time.total", a, AtomicLong::get)
                .tag("table", table).register(registry);
        return a;
    }

    public long getTableCount(String table)                     { return tableCount.getOrDefault(table, new AtomicLong()).get(); }
    public long getTableTotalTime(String table, TimeUnit unit)  { return unit.convert(tableTime.getOrDefault(table, new AtomicLong()).get(), TimeUnit.NANOSECONDS); }
    public double getTableAvgTime(String table, TimeUnit unit)  { long c = getTableCount(table); return c > 0 ? (double) getTableTotalTime(table, unit) / c : 0; }
    public long getTotalCount()                                 { return totalCount.get(); }
    public long getTotalTime(TimeUnit unit)                     { return unit.convert(totalTime.get(), TimeUnit.NANOSECONDS); }
    public double getTotalAvgTime(TimeUnit unit)                { long c = getTotalCount(); return c > 0 ? (double) getTotalTime(unit) / c : 0; }
}