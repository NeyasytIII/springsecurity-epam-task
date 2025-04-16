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

    private TraineeStorage traineeStorage;
    private TrainerStorage trainerStorage;
    private AuthenticationServiceImpl service;

    @BeforeEach
    void setUp() {
        traineeStorage = mock(TraineeStorage.class);
        trainerStorage = mock(TrainerStorage.class);
        service = new AuthenticationServiceImpl(traineeStorage, trainerStorage);
    }

    @Test
    void testAuthenticateValidTrainee() {
        Trainee trainee = new Trainee();
        trainee.setPassword("pass");
        trainee.setActive(true);
        when(traineeStorage.findByUsername("user")).thenReturn(Optional.of(trainee));
        when(trainerStorage.findByUsername("user")).thenReturn(Optional.empty());

        assertTrue(service.authenticate("user", "pass"));
    }

    @Test
    void testAuthenticateValidTrainer() {
        Trainer trainer = new Trainer();
        trainer.setPassword("pass");
        trainer.setActive(true);
        when(traineeStorage.findByUsername("user")).thenReturn(Optional.empty());
        when(trainerStorage.findByUsername("user")).thenReturn(Optional.of(trainer));

        assertTrue(service.authenticate("user", "pass"));
    }

    @Test
    void testAuthenticateInvalidCredentials() {
        when(traineeStorage.findByUsername("user")).thenReturn(Optional.empty());
        when(trainerStorage.findByUsername("user")).thenReturn(Optional.empty());

        assertFalse(service.authenticate("user", "pass"));
    }

    @Test
    void testAuthenticateWrongPassword() {
        Trainee trainee = new Trainee();
        trainee.setPassword("correct");
        trainee.setActive(true);
        when(traineeStorage.findByUsername("user")).thenReturn(Optional.of(trainee));
        when(trainerStorage.findByUsername("user")).thenReturn(Optional.empty());

        assertFalse(service.authenticate("user", "wrong"));
    }


}