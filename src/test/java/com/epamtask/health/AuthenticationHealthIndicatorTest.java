package com.epamtask.health;

import com.epamtask.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthenticationHealthIndicatorTest {

    private AuthenticationService authenticationService;
    private AuthenticationHealthIndicator indicator;

    @BeforeEach
    void setUp() {
        authenticationService = mock(AuthenticationService.class);
        indicator = new AuthenticationHealthIndicator(authenticationService);
    }

    @Test
    void shouldReturnHealthUpWhenAuthenticated() {
        when(authenticationService.authenticate("testUser", "testPassword")).thenReturn(true);

        Health health = indicator.health();

        assertThat(health.getStatus().getCode()).isEqualTo("UP");
        assertThat(health.getDetails().get("authentication")).isEqualTo("Auth system is working properly");
    }

    @Test
    void shouldReturnHealthDownWhenAuthenticationFails() {
        when(authenticationService.authenticate("testUser", "testPassword")).thenReturn(false);

        Health health = indicator.health();

        assertThat(health.getStatus().getCode()).isEqualTo("DOWN");
        assertThat(health.getDetails().get("authentication")).isEqualTo("Authentication failed");
    }

    @Test
    void shouldReturnHealthDownWhenExceptionThrown() {
        when(authenticationService.authenticate("testUser", "testPassword"))
                .thenThrow(new RuntimeException("Connection error"));

        Health health = indicator.health();

        assertThat(health.getStatus().getCode()).isEqualTo("DOWN");
        assertThat(health.getDetails().get("authentication"))
                .isEqualTo("Authentication system is down: Connection error");
    }
}