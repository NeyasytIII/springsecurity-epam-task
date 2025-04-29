package com.epamtask.service.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.security.LogoutTokenBlacklistService;
import com.epamtask.service.LogoutService;
import org.springframework.stereotype.Service;

@Service
public class LogoutServiceImpl implements LogoutService {

    private final LogoutTokenBlacklistService blacklistService;

    public LogoutServiceImpl(LogoutTokenBlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @Override
    @Loggable
    public void logout(String token) {
        blacklistService.blacklist(token);
    }

    @Override
    @Loggable
    public boolean isTokenRevoked(String token) {
        return blacklistService.isBlacklisted(token);
    }
}