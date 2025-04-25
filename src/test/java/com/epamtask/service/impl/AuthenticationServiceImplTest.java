package com.epamtask.service.impl;

import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.storege.datamodes.TraineeStorage;
import com.epamtask.storege.datamodes.TrainerStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationServiceImplTest {

    private TraineeStorage dbTraineeStorage;
    private TraineeStorage fileTraineeStorage;
    private TrainerStorage dbTrainerStorage;
    private TrainerStorage fileTrainerStorage;
    private AuthenticationServiceImpl service;

    @BeforeEach
    void setUp() {
        dbTraineeStorage = mock(TraineeStorage.class);
        fileTraineeStorage = mock(TraineeStorage.class);
        dbTrainerStorage = mock(TrainerStorage.class);
        fileTrainerStorage = mock(TrainerStorage.class);
        service = new AuthenticationServiceImpl(
                "DATABASE",
                dbTraineeStorage,
                fileTraineeStorage,
                dbTrainerStorage,
                fileTrainerStorage
        );
    }

    @Test
    void testAuthenticateValidTrainee() {
        Trainee trainee = new Trainee();
        trainee.setPassword("pass");
        when(dbTraineeStorage.findByUsername("user")).thenReturn(Optional.of(trainee));
        when(dbTrainerStorage.findByUsername("user")).thenReturn(Optional.empty());
        assertTrue(service.authenticate("user", "pass"));
    }

    @Test
    void testAuthenticateValidTrainer() {
        when(dbTraineeStorage.findByUsername("user")).thenReturn(Optional.empty());
        Trainer trainer = new Trainer();
        trainer.setPassword("pass");
        when(dbTrainerStorage.findByUsername("user")).thenReturn(Optional.of(trainer));
        assertTrue(service.authenticate("user", "pass"));
    }

    @Test
    void testAuthenticateInvalidCredentials() {
        when(dbTraineeStorage.findByUsername("user")).thenReturn(Optional.empty());
        when(dbTrainerStorage.findByUsername("user")).thenReturn(Optional.empty());
        assertFalse(service.authenticate("user", "pass"));
    }

    @Test
    void testAuthenticateWrongPassword() {
        Trainee trainee = new Trainee();
        trainee.setPassword("correct");
        when(dbTraineeStorage.findByUsername("user")).thenReturn(Optional.of(trainee));
        when(dbTrainerStorage.findByUsername("user")).thenReturn(Optional.empty());
        assertFalse(service.authenticate("user", "wrong"));
    }
}