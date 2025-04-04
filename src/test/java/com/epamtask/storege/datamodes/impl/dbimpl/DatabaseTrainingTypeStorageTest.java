package com.epamtask.storege.datamodes.impl.dbimpl;

import com.epamtask.model.TrainingTypeEntity;
import com.epamtask.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseTrainingTypeStorageTest {

    private TrainingTypeRepository trainingTypeRepository;
    private DatabaseTrainingTypeStorage storage;

    @BeforeEach
    void setUp() {
        trainingTypeRepository = mock(TrainingTypeRepository.class);
        storage = new DatabaseTrainingTypeStorage(trainingTypeRepository);
    }

    @Test
    void testFindAll() {
        when(trainingTypeRepository.findAll()).thenReturn(List.of(new TrainingTypeEntity()));
        List<TrainingTypeEntity> result = storage.findAll();
        assertEquals(1, result.size());
        verify(trainingTypeRepository).findAll();
    }

    @Test
    void testFindById() {
        TrainingTypeEntity type = new TrainingTypeEntity();
        when(trainingTypeRepository.findById(1L)).thenReturn(Optional.of(type));
        Optional<TrainingTypeEntity> result = storage.findById(1L);
        assertTrue(result.isPresent());
        verify(trainingTypeRepository).findById(1L);
    }

    @Test
    void testFindByName() {
        TrainingTypeEntity type = new TrainingTypeEntity();
        when(trainingTypeRepository.findByName("YOGA")).thenReturn(Optional.of(type));
        Optional<TrainingTypeEntity> result = storage.findByName("YOGA");
        assertTrue(result.isPresent());
        verify(trainingTypeRepository).findByName("YOGA");
    }

    @Test
    void testSaveRaw() {
        TrainingTypeEntity type = new TrainingTypeEntity();
        storage.saveRaw(type);
        verify(trainingTypeRepository).saveRaw(type);
    }
}