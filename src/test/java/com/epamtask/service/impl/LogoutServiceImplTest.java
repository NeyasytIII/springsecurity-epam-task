package com.epamtask.service.impl;

import com.epamtask.security.LogoutTokenBlacklistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LogoutServiceImplTest {

    private LogoutTokenBlacklistService blacklistService;
    private LogoutServiceImpl logoutService;

    @BeforeEach
    void setUp() {
        blacklistService = mock(LogoutTokenBlacklistService.class);
        logoutService = new LogoutServiceImpl(blacklistService);
    }

    @Test
    void logout_shouldCallBlacklist() {
        String token = "testToken";
        logoutService.logout(token);
        verify(blacklistService, times(1)).blacklist(token);
    }

    @Test
    void isTokenRevoked_shouldReturnTrue() {
        when(blacklistService.isBlacklisted("revokedToken")).thenReturn(true);
        assertTrue(logoutService.isTokenRevoked("revokedToken"));
    }

    @Test
    void isTokenRevoked_shouldReturnFalse() {
        when(blacklistService.isBlacklisted("validToken")).thenReturn(false);
        assertFalse(logoutService.isTokenRevoked("validToken"));
    }
}