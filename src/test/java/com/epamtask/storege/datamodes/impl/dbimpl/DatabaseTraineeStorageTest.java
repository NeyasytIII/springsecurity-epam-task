package com.epamtask.storege.datamodes.impl.dbimpl;

import com.epamtask.model.Trainee;
import com.epamtask.repository.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseTraineeStorageTest {

    private TraineeRepository traineeRepository;
    private DatabaseTraineeStorage storage;

    @BeforeEach
    void setUp() {
        traineeRepository = mock(TraineeRepository.class);
        storage = new DatabaseTraineeStorage(traineeRepository);
    }

    @Test
    void testFindById() {
        Trainee trainee = new Trainee();
        when(traineeRepository.findById(1L)).thenReturn(Optional.of(trainee));
        Optional<Trainee> result = storage.findById(1L);
        assertTrue(result.isPresent());
        verify(traineeRepository).findById(1L);
    }

    @Test
    void testFindByUsername() {
        Trainee trainee = new Trainee();
        when(traineeRepository.findByUsername("user")).thenReturn(Optional.of(trainee));
        Optional<Trainee> result = storage.findByUsername("user");
        assertTrue(result.isPresent());
        verify(traineeRepository).findByUsername("user");
    }

    @Test
    void testFindAll() {
        when(traineeRepository.findAll()).thenReturn(List.of(new Trainee()));
        List<Trainee> result = storage.findAll();
        assertEquals(1, result.size());
        verify(traineeRepository).findAll();
    }

    @Test
    void testSave() {
        Trainee trainee = new Trainee();
        storage.save(trainee);
        verify(traineeRepository).save(trainee);
    }

    @Test
    void testDeleteById() {
        storage.deleteById(1L);
        verify(traineeRepository).deleteById(1L);
    }

    @Test
    void testDeleteByUsername() {
        storage.deleteByUsername("user");
        verify(traineeRepository).deleteByUsername("user");
    }

    @Test
    void testUpdatePassword() {
        storage.updatePassword("user", "newpass");
        verify(traineeRepository).updatePassword("user", "newpass");
    }

    @Test
    void testActivateUser() {
        storage.activateUser("user");
        verify(traineeRepository).activateUser("user");
    }

    @Test
    void testDeactivateUser() {
        storage.deactivateUser("user");
        verify(traineeRepository).deactivateUser("user");
    }
}