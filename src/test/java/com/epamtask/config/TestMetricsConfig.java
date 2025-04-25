package com.epamtask.config;

import com.epamtask.service.metrics.ApiMetricsService;
import com.epamtask.service.metrics.AuthenticationMetricsService;
import com.epamtask.service.metrics.DatabaseMetricsService;
import io.micrometer.core.instrument.MeterRegistry;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"prod","test"})
public class TestMetricsConfig {

    @Bean
    public ApiMetricsService apiMetricsService() {
        return Mockito.mock(ApiMetricsService.class);
    }

    @Bean
    @Primary
    public AuthenticationMetricsService authenticationMetricsService() {
        return Mockito.mock(AuthenticationMetricsService.class);
    }

    @Bean
    public DatabaseMetricsService databaseMetricsService(MeterRegistry registry) {
        return new DatabaseMetricsService(registry);
    }
}