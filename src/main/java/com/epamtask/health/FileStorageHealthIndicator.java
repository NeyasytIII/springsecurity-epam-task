package com.epamtask.health;

import com.epamtask.aspect.annotation.Loggable;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev", "stg", "prod"})
public class FileStorageHealthIndicator implements HealthIndicator {

    private final ResourceLoader resourceLoader;

    public FileStorageHealthIndicator(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    @Loggable
    public Health health() {
        String[] filePaths = {
                "classpath:data/trainees.json",
                "classpath:data/trainers.json",
                "classpath:data/trainings.json"
        };

        for (String path : filePaths) {
            Resource resource = resourceLoader.getResource(path);
            if (!resource.exists() || !resource.isReadable()) {
                return Health.down()
                        .withDetail("missingFile", path)
                        .build();
            }
        }

        return Health.up()
                .withDetail("files", "All required files are accessible")
                .build();
    }
}