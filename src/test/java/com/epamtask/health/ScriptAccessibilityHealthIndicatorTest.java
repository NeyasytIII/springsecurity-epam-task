package com.epamtask.health;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScriptAccessibilityHealthIndicatorTest {

    private ResourceLoader resourceLoader;
    private ScriptAccessibilityHealthIndicator indicator;

    @BeforeEach
    void setUp() {
        resourceLoader = mock(ResourceLoader.class);
        indicator = new ScriptAccessibilityHealthIndicator(resourceLoader);
    }

    @Test
    void shouldReturnHealthUpWhenAllScriptsAccessible() {
        String[] paths = {
                "classpath:sql/trainer-data.sql",
                "classpath:sql/trainee-data.sql"
        };

        for (String path : paths) {
            Resource resource = mock(Resource.class);
            when(resource.exists()).thenReturn(true);
            when(resource.isReadable()).thenReturn(true);
            when(resourceLoader.getResource(path)).thenReturn(resource);
        }

        Health health = indicator.health();

        assertThat(health.getStatus().getCode()).isEqualTo("UP");
        assertThat(health.getDetails().get("scripts")).isEqualTo("All required scripts are accessible");
    }

    @Test
    void shouldReturnHealthDownWhenScriptMissing() {
        Resource resource = mock(Resource.class);
        when(resource.exists()).thenReturn(false);
        when(resourceLoader.getResource("classpath:sql/trainer-data.sql")).thenReturn(resource);

        Health health = indicator.health();

        assertThat(health.getStatus().getCode()).isEqualTo("DOWN");
        assertThat(health.getDetails().get("missingScript")).isEqualTo("classpath:sql/trainer-data.sql");
    }
}