package com.epamtask.utils;

import com.epamtask.aspect.annotation.Loggable;

import java.util.UUID;

public class PasswordGenerator {
    @Loggable
    public static String generatePassword() {
        String password = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        if (password.length() < 10) {
            password = password + UUID.randomUUID().toString().replace("-", "").substring(0, 10 - password.length());
        }
        System.out.println("Generated password length: " + password.length());
        return password;
    }
}