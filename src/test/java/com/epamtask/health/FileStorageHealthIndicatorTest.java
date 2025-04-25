package com.epamtask.health;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileStorageHealthIndicatorTest {

    private ResourceLoader resourceLoader;
    private FileStorageHealthIndicator indicator;

    @BeforeEach
    void setUp() {
        resourceLoader = mock(ResourceLoader.class);
        indicator = new FileStorageHealthIndicator(resourceLoader);
    }

    @Test
    void shouldReturnHealthUpWhenAllFilesAccessible() {
        String[] paths = {
                "classpath:data/trainees.json",
                "classpath:data/trainers.json",
                "classpath:data/trainings.json"
        };

        for (String path : paths) {
            Resource resource = mock(Resource.class);
            when(resource.exists()).thenReturn(true);
            when(resource.isReadable()).thenReturn(true);
            when(resourceLoader.getResource(path)).thenReturn(resource);
        }

        Health health = indicator.health();

        assertThat(health.getStatus().getCode()).isEqualTo("UP");
        assertThat(health.getDetails().get("files")).isEqualTo("All required files are accessible");
    }

    @Test
    void shouldReturnHealthDownWhenFileMissing() {
        Resource resource = mock(Resource.class);
        when(resource.exists()).thenReturn(false);
        when(resourceLoader.getResource("classpath:data/trainees.json")).thenReturn(resource);

        Health health = indicator.health();

        assertThat(health.getStatus().getCode()).isEqualTo("DOWN");
        assertThat(health.getDetails().get("missingFile")).isEqualTo("classpath:data/trainees.json");
    }
}