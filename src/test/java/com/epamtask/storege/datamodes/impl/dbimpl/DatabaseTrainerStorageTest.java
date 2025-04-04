package com.epamtask.storege.datamodes.impl.dbimpl;

import com.epamtask.model.Trainer;
import com.epamtask.repository.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseTrainerStorageTest {

    private TrainerRepository trainerRepository;
    private DatabaseTrainerStorage storage;

    @BeforeEach
    void setUp() {
        trainerRepository = mock(TrainerRepository.class);
        storage = new DatabaseTrainerStorage(trainerRepository);
    }

    @Test
    void testFindById() {
        Trainer trainer = new Trainer();
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));
        Optional<Trainer> result = storage.findById(1L);
        assertTrue(result.isPresent());
        verify(trainerRepository).findById(1L);
    }

    @Test
    void testFindByUsername() {
        Trainer trainer = new Trainer();
        when(trainerRepository.findByUsername("user")).thenReturn(Optional.of(trainer));
        Optional<Trainer> result = storage.findByUsername("user");
        assertTrue(result.isPresent());
        verify(trainerRepository).findByUsername("user");
    }

    @Test
    void testFindAll() {
        when(trainerRepository.findAll()).thenReturn(List.of(new Trainer()));
        List<Trainer> result = storage.findAll();
        assertEquals(1, result.size());
        verify(trainerRepository).findAll();
    }

    @Test
    void testSave() {
        Trainer trainer = new Trainer();
        storage.save(trainer);
        verify(trainerRepository).save(trainer);
    }

    @Test
    void testUpdate() {
        Trainer trainer = new Trainer();
        storage.update(trainer);
        verify(trainerRepository).save(trainer);
    }

    @Test
    void testDeleteById() {
        storage.deleteById(1L);
        verify(trainerRepository).deleteById(1L);
    }
}