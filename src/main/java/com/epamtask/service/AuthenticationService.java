package com.epamtask.service;

public interface AuthenticationService {
    boolean authenticate(String username, String rawPassword);

    void changePassword(String username, String oldPassword, String newPassword);

    void register(String username, String rawPassword, String role);
}