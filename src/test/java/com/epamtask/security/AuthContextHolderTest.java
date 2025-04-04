package com.epamtask.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthContextHolderTest {

    @Test
    void testSetAndGetCredentials() {
        AuthContextHolder.setCredentials("user1", "pass1");
        assertEquals("user1", AuthContextHolder.getUsername());
        assertEquals("pass1", AuthContextHolder.getPassword());
        AuthContextHolder.clear();
    }

    @Test
    void testClearCredentials() {
        AuthContextHolder.setCredentials("user2", "pass2");
        AuthContextHolder.clear();
        assertThrows(SecurityException.class, AuthContextHolder::getUsername);
        assertThrows(SecurityException.class, AuthContextHolder::getPassword);
    }

    @Test
    void testThreadIsolation() throws InterruptedException {
        AuthContextHolder.setCredentials("mainUser", "mainPass");

        Thread thread = new Thread(() -> {
            AuthContextHolder.setCredentials("threadUser", "threadPass");
            assertEquals("threadUser", AuthContextHolder.getUsername());
            assertEquals("threadPass", AuthContextHolder.getPassword());
            AuthContextHolder.clear();
            assertThrows(SecurityException.class, AuthContextHolder::getUsername);
            assertThrows(SecurityException.class, AuthContextHolder::getPassword);
        });

        thread.start();
        thread.join();

        assertEquals("mainUser", AuthContextHolder.getUsername());
        assertEquals("mainPass", AuthContextHolder.getPassword());

        AuthContextHolder.clear();

        assertThrows(SecurityException.class, AuthContextHolder::getUsername);
        assertThrows(SecurityException.class, AuthContextHolder::getPassword);
    }
}