package com.epamtask.health;

import com.epamtask.aspect.annotation.Loggable;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class ScriptAccessibilityHealthIndicator implements HealthIndicator {

    private final ResourceLoader resourceLoader;

    public ScriptAccessibilityHealthIndicator(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    @Loggable
    public Health health() {
        String[] scriptPaths = {
                "classpath:sql/trainer-data.sql",
                "classpath:sql/trainee-data.sql"
        };

        for (String path : scriptPaths) {
            Resource resource = resourceLoader.getResource(path);
            if (!resource.exists() || !resource.isReadable()) {
                return Health.down()
                        .withDetail("missingScript", path)
                        .build();
            }
        }

        return Health.up()
                .withDetail("scripts", "All required scripts are accessible")
                .build();
    }
}