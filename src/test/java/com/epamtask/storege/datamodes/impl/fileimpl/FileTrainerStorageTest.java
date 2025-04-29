package com.epamtask.storege.datamodes.impl.fileimpl;

import com.epamtask.dao.TrainerDAO;
import com.epamtask.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FileTrainerStorageTest {

    private TrainerDAO trainerDAO;
    private FileTrainerStorage storage;

    @BeforeEach
    void setUp() {
        trainerDAO = mock(TrainerDAO.class);
        storage = new FileTrainerStorage(trainerDAO);
    }

    @Test
    void testSave() {
        Trainer trainer = new Trainer();
        trainer.setTrainerId(1L);
        storage.save(trainer);
        verify(trainerDAO).create(1L, trainer);
    }

    @Test
    void testFindById() {
        Trainer trainer = new Trainer();
        when(trainerDAO.findById(1L)).thenReturn(Optional.of(trainer));
        Optional<Trainer> result = storage.findById(1L);
        assertTrue(result.isPresent());
        verify(trainerDAO).findById(1L);
    }

    @Test
    void testFindByUsername() {
        Trainer trainer = new Trainer();
        when(trainerDAO.findByUsername("user")).thenReturn(Optional.of(trainer));
        Optional<Trainer> result = storage.findByUsername("user");
        assertTrue(result.isPresent());
        verify(trainerDAO).findByUsername("user");
    }

    @Test
    void testFindAll() {
        Map<Long, Trainer> data = Map.of(1L, new Trainer());
        when(trainerDAO.getAll()).thenReturn(data);
        List<Trainer> result = storage.findAll();
        assertEquals(1, result.size());
        verify(trainerDAO).getAll();
    }

    @Test
    void testDeleteById() {
        storage.deleteById(3L);
        verify(trainerDAO).deleteById(3L);
    }
}