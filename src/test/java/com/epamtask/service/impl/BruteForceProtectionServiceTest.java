package com.epamtask.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BruteForceProtectionServiceTest {

    private BruteForceProtectionService service;

    @BeforeEach
    void setUp() {
        service = new BruteForceProtectionService();
    }

    @Test
    void recordFailedAttempt_shouldLockAfterMaxAttempts() {
        String username = "testUser";

        service.recordFailedAttempt(username);
        service.recordFailedAttempt(username);
        assertFalse(service.isLocked(username));

        service.recordFailedAttempt(username);
        assertTrue(service.isLocked(username));
    }

    @Test
    void resetAttempts_shouldUnlockUser() {
        String username = "lockedUser";

        service.recordFailedAttempt(username);
        service.recordFailedAttempt(username);
        service.recordFailedAttempt(username);

        assertTrue(service.isLocked(username));

        service.resetAttempts(username);

        assertFalse(service.isLocked(username));
    }
}