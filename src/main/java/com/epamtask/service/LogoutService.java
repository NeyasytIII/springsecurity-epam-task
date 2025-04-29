package com.epamtask.service;

public interface LogoutService {
    void logout(String token);
    boolean isTokenRevoked(String token);
}