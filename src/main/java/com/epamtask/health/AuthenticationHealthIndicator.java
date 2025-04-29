package com.epamtask.health;

import com.epamtask.service.AuthenticationService;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHealthIndicator implements HealthIndicator {

    private final AuthenticationService authenticationService;

    public AuthenticationHealthIndicator(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Health health() {
        try {
            boolean isAuthenticated = authenticationService.authenticate("testUser", "testPassword");

            if (isAuthenticated) {
                return Health.up().withDetail("authentication", "Auth system is working properly").build();
            } else {
                return Health.down().withDetail("authentication", "Authentication failed").build();
            }
        } catch (Exception e) {
            return Health.down().withDetail("authentication", "Authentication system is down: " + e.getMessage()).build();
        }
    }
}