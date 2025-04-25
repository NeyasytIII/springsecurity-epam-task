package com.epamtask.health;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.security.AuthSessionStore;
import com.epamtask.security.AuthSessionStore.Credentials;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile({"dev", "prod"})
public class AuthenticationHealthIndicator implements HealthIndicator {

    private final AuthSessionStore authSessionStore;

    public AuthenticationHealthIndicator(AuthSessionStore authSessionStore) {
        this.authSessionStore = authSessionStore;
    }

    @Override
    @Loggable
    public Health health() {
        try {
            String token = authSessionStore.createToken("testUser", "testPassword");
            Credentials credentials = authSessionStore.getCredentials(token);

            if (credentials != null) {
                return Health.up().withDetail("authentication", "Auth system is working properly").build();
            }
        } catch (SecurityException e) {
            return Health.down().withDetail("authentication", "Authentication failed").build();
        }

        return Health.down().withDetail("authentication", "Token is invalid").build();
    }
}