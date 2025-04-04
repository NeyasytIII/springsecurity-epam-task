package com.epamtask.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {

    @Test
    void testGeneratePasswordLength() {
        String password = PasswordGenerator.generatePassword();
        assertNotNull(password);
        assertEquals(10, password.length());
    }

    @Test
    void testGeneratePasswordRandomness() {
        String password1 = PasswordGenerator.generatePassword();
        String password2 = PasswordGenerator.generatePassword();
        assertNotEquals(password1, password2);
    }
}