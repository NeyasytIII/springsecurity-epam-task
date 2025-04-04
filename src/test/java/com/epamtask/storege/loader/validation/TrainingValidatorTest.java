package com.epamtask.storege.loader.validation;

import com.epamtask.model.*;
import com.epamtask.storege.loader.validation.common.DateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrainingValidatorTest {

    private TrainingValidator validator;

    @BeforeEach
    void setUp() {
        DateValidator dateValidator = new DateValidator();
        validator = new TrainingValidator(dateValidator);
    }

    @Test
    void validTraining() {
        Trainee trainee = new Trainee(1L, "Alice", "Smith", "Addr", new Date(System.currentTimeMillis() - 1000), true);
        Trainer trainer = new Trainer(2L, "John", "Doe", "Fitness", true);
        TrainingType trainingType = TrainingType.STRENGTH;
        Date trainingDate = new Date(System.currentTimeMillis() - 2000);
        String trainingDuration = "60 minutes";

        Training training = new Training(100L, "Yoga", new TrainingTypeEntity(trainingType), trainingDate, trainingDuration);
        training.setTrainee(trainee);
        training.setTrainer(trainer);

        Map<Long, Trainee> traineeStorage = new HashMap<>();
        traineeStorage.put(1L, trainee);

        Map<Long, Trainer> trainerStorage = new HashMap<>();
        trainerStorage.put(2L, trainer);

        List<String> errors = validator.validate(List.of(training), traineeStorage, trainerStorage);
        assertTrue(errors.isEmpty());
    }

    @Test
    void invalidTraining() {
        Trainee trainee = new Trainee(1L, "Alice", "Smith", "Addr", new Date(System.currentTimeMillis() - 1000), true);
        Trainer trainer = new Trainer(2L, "John", "Doe", "Fitness", true);
        TrainingType trainingType = null;
        Date trainingDate = new Date(System.currentTimeMillis() + 100000);
        String trainingDuration = "";

        Training training = new Training(0L, "", null, trainingDate, trainingDuration);
        training.setTrainee(trainee);
        training.setTrainer(trainer);

        Map<Long, Trainee> traineeStorage = new HashMap<>();
        traineeStorage.put(1L, trainee);

        Map<Long, Trainer> trainerStorage = new HashMap<>();
        trainerStorage.put(2L, trainer);

        List<String> errors = validator.validate(List.of(training), traineeStorage, trainerStorage);
        assertFalse(errors.isEmpty());
        assertTrue(errors.stream().anyMatch(msg -> msg.contains("Invalid training ID")));
        assertTrue(errors.stream().anyMatch(msg -> msg.contains("Invalid or missing training date")));
        assertTrue(errors.stream().anyMatch(msg -> msg.contains("Training name is invalid")));
        assertTrue(errors.stream().anyMatch(msg -> msg.contains("Training type is invalid")));
        assertTrue(errors.stream().anyMatch(msg -> msg.contains("Training duration is invalid")));
    }
}