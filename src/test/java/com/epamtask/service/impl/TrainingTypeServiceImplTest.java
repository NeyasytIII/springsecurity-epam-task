package com.epamtask.service.impl;

import com.epamtask.model.TrainingType;
import com.epamtask.model.TrainingTypeEntity;
import com.epamtask.storege.datamodes.TrainingTypeStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class TrainingTypeServiceImplTest {

    @Mock
    private TrainingTypeStorage trainingTypeStorage;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainingTypeService = new TrainingTypeServiceImpl("DATABASE", trainingTypeStorage);
    }

    @Test
    void testGetAllTrainingTypes_Success() {
        List<TrainingTypeEntity> types = List.of(new TrainingTypeEntity(TrainingType.YOGA));
        when(trainingTypeStorage.findAll()).thenReturn(types);

        List<TrainingTypeEntity> result = trainingTypeService.getAllTrainingTypes();

        assertEquals(1, result.size());
    }

    @Test
    void testGetAllTrainingTypes_Empty_ShouldThrowException() {
        when(trainingTypeStorage.findAll()).thenReturn(Collections.emptyList());

        assertThrows(IllegalStateException.class, () -> trainingTypeService.getAllTrainingTypes());
    }

    @Test
    void testGetTrainingTypeById_Success() {
        TrainingTypeEntity entity = new TrainingTypeEntity(TrainingType.CARDIO);
        when(trainingTypeStorage.findById(1L)).thenReturn(Optional.of(entity));

        Optional<TrainingTypeEntity> result = trainingTypeService.getTrainingTypeById(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void testGetTrainingTypeById_InvalidId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> trainingTypeService.getTrainingTypeById(null));
        assertThrows(IllegalArgumentException.class, () -> trainingTypeService.getTrainingTypeById(0L));
    }

    @Test
    void testGetTrainingTypeByName_Success() {
        TrainingTypeEntity entity = new TrainingTypeEntity(TrainingType.CROSSFIT);
        when(trainingTypeStorage.findByName("Crossfit")).thenReturn(Optional.of(entity));

        Optional<TrainingTypeEntity> result = trainingTypeService.getTrainingTypeByName("Crossfit");

        assertTrue(result.isPresent());
    }

    @Test
    void testGetTrainingTypeByName_InvalidName_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> trainingTypeService.getTrainingTypeByName(null));
        assertThrows(IllegalArgumentException.class, () -> trainingTypeService.getTrainingTypeByName(" "));
    }
}