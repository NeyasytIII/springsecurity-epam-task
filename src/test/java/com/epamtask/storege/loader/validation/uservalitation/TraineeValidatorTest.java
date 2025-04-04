package com.epamtask.storege.loader.validation.uservalitation;

import com.epamtask.model.Trainee;
import com.epamtask.storege.loader.validation.common.DateValidator;
import com.epamtask.storege.loader.validation.uservalidation.TraineeValidator;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TraineeValidatorTest {
    private final DateValidator dateValidator = new DateValidator();
    private final TraineeValidator traineeValidator = new TraineeValidator(dateValidator);

    @Test
    void testValidTrainee() {
        Trainee trainee = new Trainee(1L, "John", "Doe", "Address", new Date(System.currentTimeMillis() - 10000),true);
        trainee.setUserName("John.Doe");
        trainee.setPassword("secret");
        List<String> errors = traineeValidator.validate(List.of(trainee));
        assertTrue(errors.isEmpty());
    }

    @Test
    void testInvalidTrainee() {
        Trainee trainee = new Trainee(null, "", " ", "Address", new Date(System.currentTimeMillis() + 10000),true);
        trainee.setUserName("");
        trainee.setPassword("");
        List<String> errors = traineeValidator.validate(List.of(trainee));
        assertFalse(errors.isEmpty());
        assertTrue(errors.contains("Invalid trainee ID"));
        assertTrue(errors.contains("First name is invalid"));
        assertTrue(errors.contains("Last name is invalid"));
        assertTrue(errors.contains("UserName is invalid"));
        assertTrue(errors.contains("Password is invalid"));
        assertTrue(errors.contains("Invalid or missing birthday date"));
    }
}