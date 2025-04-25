package com.epamtask.service.metrics;

import com.epamtask.aspect.annotation.Loggable;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AuthenticationMetricsService {

    private final MeterRegistry registry;
    private final Counter success;
    private final Counter fail;
    private final Timer loginTimer;
    private final Counter lock;
    private final Gauge activeGauge;
    private final DistributionSummary repeatFail;
    private final AtomicInteger active = new AtomicInteger(0);
    private final Map<String, AtomicInteger> perUserFail = new ConcurrentHashMap<>();
    private final List<String> failedUsers = Collections.synchronizedList(new ArrayList<>());

    public AuthenticationMetricsService(MeterRegistry registry) {
        this.registry = registry;
        this.success = Counter.builder("custom.auth.login.success").register(registry);
        this.fail = Counter.builder("custom.auth.login.failure").register(registry);
        this.loginTimer = Timer.builder("custom.auth.login.time").publishPercentileHistogram().publishPercentiles(0.5, 0.95, 0.99).minimumExpectedValue(Duration.ofMillis(50)).maximumExpectedValue(Duration.ofSeconds(10)).register(registry);
        this.lock = Counter.builder("custom.auth.login.lock").register(registry);
        this.activeGauge = Gauge.builder("custom.auth.active", active, AtomicInteger::get).register(registry);
        this.repeatFail = DistributionSummary.builder("custom.auth.repeat.failure").baseUnit("attempts").publishPercentileHistogram().register(registry);
    }
    @Loggable
    public void success() { success.increment(); }
    @Loggable
    public void fail(String user) {
        fail.increment();
        failedUsers.add(user);
        perUserFail.computeIfAbsent(user, k -> new AtomicInteger()).incrementAndGet();
        repeatFail.record(perUserFail.get(user).get());
    }
    @Loggable
    public void lock(String user) {
        lock.increment();
        Counter.builder("custom.auth.lock.user").tag("user", user).register(registry).increment();
    }
    @Loggable
    public <T> T timed(java.util.concurrent.Callable<T> call) throws Exception {
        active.incrementAndGet();
        try { return loginTimer.recordCallable(call); } finally { active.decrementAndGet(); }
    }
}