package com.epamtask.security;

import com.epamtask.aspect.annotation.Loggable;

public class AuthContextHolder {

    private static final ThreadLocal<String> currentUsername = new ThreadLocal<>();
    private static final ThreadLocal<String> currentPassword = new ThreadLocal<>();

    public static void setCredentials(String username, String password) {
        currentUsername.set(username);
        currentPassword.set(password);
    }

    @Loggable
    public static String getUsername() {
        String username = currentUsername.get();
        if (username == null) {
            throw new SecurityException("No username in context");
        }
        return username;
    }

    @Loggable
    public static String getPassword() {
        String password = currentPassword.get();
        if (password == null) {
            throw new SecurityException("No password in context");
        }
        return password;
    }

    @Loggable
    public static void clear() {
        currentUsername.remove();
        currentPassword.remove();
    }
}