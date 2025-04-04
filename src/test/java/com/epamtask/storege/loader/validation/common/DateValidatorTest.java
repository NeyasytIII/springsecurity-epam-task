package com.epamtask.storege.loader.validation.common;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class DateValidatorTest {
    private final DateValidator validator = new DateValidator();

    @Test
    void validDate() {
        Date past = new Date(System.currentTimeMillis() - 1000);
        assertTrue(validator.isDateValid(past));
    }

    @Test
    void nullDate() {
        assertFalse(validator.isDateValid(null));
    }

    @Test
    void futureDate() {
        Date future = new Date(System.currentTimeMillis() + 100000);
        assertFalse(validator.isDateValid(future));
    }
}