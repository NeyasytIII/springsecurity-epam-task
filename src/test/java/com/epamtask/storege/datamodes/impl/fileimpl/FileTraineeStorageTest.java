package com.epamtask.storege.datamodes.impl.fileimpl;

import com.epamtask.dao.TraineeDAO;
import com.epamtask.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileTraineeStorageTest {

    private TraineeDAO traineeDAO;
    private FileTraineeStorage storage;

    @BeforeEach
    void setUp() {
        traineeDAO = mock(TraineeDAO.class);
        storage = new FileTraineeStorage(traineeDAO);
    }

    @Test
    void testSave() {
        Trainee trainee = new Trainee();
        trainee.setTraineeId(1L);
        storage.save(trainee);
        verify(traineeDAO).create(1L, trainee);
    }

    @Test
    void testFindById() {
        Trainee trainee = new Trainee();
        when(traineeDAO.findById(1L)).thenReturn(Optional.of(trainee));
        Optional<Trainee> result = storage.findById(1L);
        assertTrue(result.isPresent());
        verify(traineeDAO).findById(1L);
    }

    @Test
    void testFindByUsername() {
        Trainee trainee = new Trainee();
        when(traineeDAO.findByUsername("user")).thenReturn(Optional.of(trainee));
        Optional<Trainee> result = storage.findByUsername("user");
        assertTrue(result.isPresent());
        verify(traineeDAO).findByUsername("user");
    }

    @Test
    void testFindAll() {
        Map<Long, Trainee> data = Map.of(1L, new Trainee());
        when(traineeDAO.getAll()).thenReturn(data);
        List<Trainee> result = storage.findAll();
        assertEquals(1, result.size());
        verify(traineeDAO).getAll();
    }

    @Test
    void testDeleteById() {
        storage.deleteById(2L);
        verify(traineeDAO).deleteById(2L);
    }

    @Test
    void testDeleteByUsername() {
        storage.deleteByUsername("user");
        verify(traineeDAO).deleteByUsername("user");
    }

    @Test
    void testUpdatePassword() {
        storage.updatePassword("user", "newPass");
        verify(traineeDAO).updatePassword("user", "newPass");
    }

    @Test
    void testActivateUser() {
        storage.activateUser("user");
        verify(traineeDAO).activateUser("user");
    }

    @Test
    void testDeactivateUser() {
        storage.deactivateUser("user");
        verify(traineeDAO).deactivateUser("user");
    }
}