package com.epamtask.service.impl;

import com.epamtask.aspect.annotation.Loggable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BruteForceProtectionService {

    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int LOCK_DURATION_MINUTES = 5;

    private final Map<String, Integer> failedAttempts = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lockedUsers = new ConcurrentHashMap<>();
    @Loggable
    public void recordFailedAttempt(String username) {
        failedAttempts.merge(username, 1, Integer::sum);
        if (failedAttempts.get(username) >= MAX_FAILED_ATTEMPTS) {
            lockedUsers.put(username, LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
            failedAttempts.remove(username);
        }
    }
    @Loggable
    public void resetAttempts(String username) {
        failedAttempts.remove(username);
        lockedUsers.remove(username);
    }
    @Loggable
    public boolean isLocked(String username) {
        LocalDateTime lockTime = lockedUsers.get(username);
        if (lockTime == null) {
            return false;
        }
        if (lockTime.isBefore(LocalDateTime.now())) {
            lockedUsers.remove(username);
            return false;
        }
        return true;
    }
}