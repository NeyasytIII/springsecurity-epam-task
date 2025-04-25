package com.epamtask.config;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
public class ConnectionPoolMetricsConfig {

    public ConnectionPoolMetricsConfig(@Qualifier("dataSource") DataSource ds, MeterRegistry reg) {
        if (ds instanceof HikariDataSource h) {
            HikariPoolMXBean b = h.getHikariPoolMXBean();
            Gauge.builder("custom.db.pool.active", b, HikariPoolMXBean::getActiveConnections).register(reg);
            Gauge.builder("custom.db.pool.idle",   b, HikariPoolMXBean::getIdleConnections).register(reg);
            Gauge.builder("custom.db.pool.wait",   b, HikariPoolMXBean::getThreadsAwaitingConnection).register(reg);
            Gauge.builder("custom.db.pool.total",  b, HikariPoolMXBean::getTotalConnections).register(reg);
        }
    }
}