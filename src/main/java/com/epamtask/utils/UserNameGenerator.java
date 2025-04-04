package com.epamtask.utils;

import com.epamtask.aspect.annotation.Loggable;
import org.springframework.stereotype.Component;

@Component
public class UserNameGenerator {

    @Loggable
    public String generateUserName(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("First and last name must be provided and cannot be empty.");
        }

        return capitalize(firstName) + "." + capitalize(lastName);
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}