package com.epamtask.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserNameGeneratorTest {

    private final UserNameGenerator generator = new UserNameGenerator();

    @Test
    void testGenerateUserNameValid() {
        String userName = generator.generateUserName("john", "doe");
        assertEquals("John.Doe", userName);
    }

    @Test
    void testGenerateUserNameWithDifferentCasing() {
        String userName = generator.generateUserName("JoHN", "DoE");
        assertEquals("John.Doe", userName);
    }

    @Test
    void testGenerateUserNameNullFirstName() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateUserName(null, "Doe"));
    }

    @Test
    void testGenerateUserNameNullLastName() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateUserName("John", null));
    }

    @Test
    void testGenerateUserNameEmptyFirstName() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateUserName("", "Doe"));
    }

    @Test
    void testGenerateUserNameEmptyLastName() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateUserName("John", ""));
    }

    @Test
    void testGenerateUserNameBlankFirstName() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateUserName("   ", "Doe"));
    }

    @Test
    void testGenerateUserNameBlankLastName() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateUserName("John", "   "));
    }
}