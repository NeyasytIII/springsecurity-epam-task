package com.epamtask.security;

import com.epamtask.aspect.annotation.Loggable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class LogoutTokenBlacklistService {

    private final Set<String> blacklist = new HashSet<>();
    @Loggable
    public void blacklist(String token) {
        blacklist.add(token);
    }
    @Loggable
    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }
}