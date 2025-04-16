package com.epamtask.security;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthSessionStoreTest {

    private AuthSessionStore sessionStore;

    @BeforeEach
    void setUp() {
        sessionStore = new AuthSessionStore();
    }

    @Test
    void createToken_shouldStoreAndReturnToken() {
        String token = sessionStore.createToken("user1", "pass123");

        assertNotNull(token);
        AuthSessionStore.Credentials credentials = sessionStore.getCredentials(token);
        assertEquals("user1", credentials.getUsername());
        assertEquals("pass123", credentials.getPassword());
    }

    @Test
    void getCredentials_invalidToken_shouldThrowSecurityException() {
        String invalidToken = "non-existent-token";

        SecurityException ex = assertThrows(SecurityException.class, () ->
                sessionStore.getCredentials(invalidToken));

        assertEquals("Access denied: invalid or missing token", ex.getMessage());
    }
}