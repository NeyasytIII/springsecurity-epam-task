package com.epamtask.storege.loader.validation.uservalitation;

import com.epamtask.model.Trainer;
import com.epamtask.storege.loader.validation.uservalidation.TrainerValidator;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TrainerValidatorTest {
    private final TrainerValidator trainerValidator = new TrainerValidator();

    @Test
    void testValidTrainer() {
        Trainer trainer = new Trainer(1L, "Alice", "Smith", "Yoga",true);
        trainer.setUserName("Alice.Smith");
        trainer.setPassword("secret");
        List<String> errors = trainerValidator.validate(List.of(trainer));
        assertTrue(errors.isEmpty());
    }

    @Test
    void testInvalidTrainer() {
        Trainer trainer = new Trainer(null, "", " ", "",true);
        trainer.setUserName("");
        trainer.setPassword("");
        List<String> errors = trainerValidator.validate(List.of(trainer));
        assertFalse(errors.isEmpty());
        assertTrue(errors.contains("Invalid trainer ID"));
        assertTrue(errors.contains("First name is invalid"));
        assertTrue(errors.contains("Last name is invalid"));
        assertTrue(errors.contains("UserName is invalid"));
        assertTrue(errors.contains("Password is invalid"));
        assertTrue(errors.contains("Specialization is invalid"));
    }
}