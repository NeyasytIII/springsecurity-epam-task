package com.epamtask.health;

import com.epamtask.security.AuthSessionStore;
import com.epamtask.security.AuthSessionStore.Credentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationHealthIndicatorTest {

    private AuthSessionStore authSessionStore;
    private AuthenticationHealthIndicator indicator;

    @BeforeEach
    void setUp() {
        authSessionStore = mock(AuthSessionStore.class);
        indicator = new AuthenticationHealthIndicator(authSessionStore);
    }

    @Test
    void shouldReturnHealthUpWhenCredentialsValid() {
        String token = "token123";
        Credentials credentials = new Credentials("testUser", "testPassword");
        when(authSessionStore.createToken("testUser", "testPassword")).thenReturn(token);
        when(authSessionStore.getCredentials(token)).thenReturn(credentials);

        Health health = indicator.health();

        assertThat(health.getStatus().getCode()).isEqualTo("UP");
        assertThat(health.getDetails().get("authentication")).isEqualTo("Auth system is working properly");
    }

    @Test
    void shouldReturnHealthDownWhenTokenIsInvalid() {
        String token = "token123";
        when(authSessionStore.createToken("testUser", "testPassword")).thenReturn(token);
        when(authSessionStore.getCredentials(token)).thenReturn(null);

        Health health = indicator.health();

        assertThat(health.getStatus().getCode()).isEqualTo("DOWN");
        assertThat(health.getDetails().get("authentication")).isEqualTo("Token is invalid");
    }

    @Test
    void shouldReturnHealthDownWhenExceptionThrown() {
        when(authSessionStore.createToken("testUser", "testPassword")).thenThrow(new SecurityException("fail"));

        Health health = indicator.health();

        assertThat(health.getStatus().getCode()).isEqualTo("DOWN");
        assertThat(health.getDetails().get("authentication")).isEqualTo("Authentication failed");
    }
}