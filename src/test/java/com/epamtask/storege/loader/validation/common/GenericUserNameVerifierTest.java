package com.epamtask.storege.loader.validation.common;

import com.epamtask.model.Trainer;
import com.epamtask.model.Trainee;
import com.epamtask.utils.UserNameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GenericUserNameVerifierTest {
    private GenericUserNameVerifier<Trainee> verifierTrainee;
    private GenericUserNameVerifier<Trainer> verifierTrainer;
    private UserNameGenerator userNameGenerator;

    @BeforeEach
    void setUp() {
        userNameGenerator = new UserNameGenerator() {
            @Override
            public String generateUserName(String firstName, String lastName) {
                return firstName + "." + lastName;
            }
        };
        verifierTrainee = new GenericUserNameVerifier<>(userNameGenerator);
        verifierTrainer = new GenericUserNameVerifier<>(userNameGenerator);
    }

    @Test
    void testVerifyUserNames_Trainee_Success() {
        Trainee trainee1 = new Trainee(1L, "John", "Doe", "addr", new Date(System.currentTimeMillis() - 10000), true);
        trainee1.setUserName("John.Doe");
        Trainee trainee2 = new Trainee(2L, "Jane", "Smith", "addr", new Date(System.currentTimeMillis() - 10000), true);
        trainee2.setUserName("Jane.Smith");
        List<Trainee> trainees = List.of(trainee1, trainee2);
        Map<Long, Trainee> storage = new HashMap<>();
        storage.put(1L, trainee1);
        storage.put(2L, trainee2);
        List<String> errors = verifierTrainee.verifyUserNames(trainees);
        assertTrue(errors.isEmpty());
    }

    @Test
    void testVerifyUserNames_Trainee_Duplicate() {
        Trainee trainee1 = new Trainee(1L, "John", "Doe", "addr", new Date(System.currentTimeMillis() - 10000), true);
        trainee1.setUserName("John.Doe");
        Trainee trainee2 = new Trainee(1L, "Jane", "Smith", "addr", new Date(System.currentTimeMillis() - 10000), true);
        trainee2.setUserName("Jane.Smith");
        List<Trainee> trainees = List.of(trainee1, trainee2);
        Map<Long, Trainee> storage = new HashMap<>();
        storage.put(1L, trainee1);
        List<String> errors = verifierTrainee.verifyUserNames(trainees);
        assertFalse(errors.isEmpty());
        assertTrue(errors.get(0).contains("Duplicate"));
    }

    @Test
    void testVerifyUserNames_Trainee_Mismatch() {
        Trainee trainee1 = new Trainee(1L, "John", "Doe", "addr", new Date(System.currentTimeMillis() - 10000), true);
        trainee1.setUserName("WrongName");
        List<Trainee> trainees = List.of(trainee1);
        Map<Long, Trainee> storage = new HashMap<>();
        storage.put(1L, trainee1);
        List<String> errors = verifierTrainee.verifyUserNames(trainees);
        assertFalse(errors.isEmpty());
        assertTrue(errors.get(0).contains("UserName mismatch"));
    }

    @Test
    void testVerifyUserNames_Trainer_Success() {
        Trainer trainer1 = new Trainer(10L, "Alice", "Brown", "Fitness", true);
        trainer1.setUserName("Alice.Brown");
        Trainer trainer2 = new Trainer(20L, "Bob", "White", "Yoga", true);
        trainer2.setUserName("Bob.White");
        List<Trainer> trainers = List.of(trainer1, trainer2);
        Map<Long, Trainer> storage = new HashMap<>();
        storage.put(10L, trainer1);
        storage.put(20L, trainer2);
        List<String> errors = verifierTrainer.verifyUserNames(trainers);
        assertTrue(errors.isEmpty());
    }

    @Test
    void testVerifyUserNames_Trainer_Mismatch() {
        Trainer trainer1 = new Trainer(10L, "Alice", "Brown", "Fitness", true);
        trainer1.setUserName("Wrong");
        List<Trainer> trainers = List.of(trainer1);
        Map<Long, Trainer> storage = new HashMap<>();
        storage.put(10L, trainer1);
        List<String> errors = verifierTrainer.verifyUserNames(trainers);
        assertFalse(errors.isEmpty());
        assertTrue(errors.get(0).contains("UserName mismatch"));
    }
}