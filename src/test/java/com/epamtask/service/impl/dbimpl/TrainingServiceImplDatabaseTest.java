package com.epamtask.service.impl.dbimpl;

import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import com.epamtask.model.TrainingType;
import com.epamtask.service.TraineeService;
import com.epamtask.service.TrainerService;
import com.epamtask.service.TrainingTypeService;
import com.epamtask.service.impl.TrainingServiceImpl;
import com.epamtask.storege.datamodes.TrainingStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceImplDatabaseTest {

    private TrainingStorage databaseTrainingStorage;
    private TrainerService trainerService;
    private TraineeService traineeService;
    private TrainingTypeService trainingTypeService;
    private TrainingServiceImpl trainingService;

    @BeforeEach
    void setUp() {
        databaseTrainingStorage = mock(TrainingStorage.class);
        trainerService = mock(TrainerService.class);
        traineeService = mock(TraineeService.class);
        trainingTypeService = mock(TrainingTypeService.class);

        trainingService = new TrainingServiceImpl(
                "DATABASE",
                databaseTrainingStorage,
                mock(TrainingStorage.class),
                trainingTypeService,
                trainerService,
                traineeService
        );
    }

    @Test
    void testCreateTraining_Success() {
        Long trainingId = 1L;
        when(databaseTrainingStorage.findById(trainingId)).thenReturn(Optional.empty());
        when(trainerService.getTrainerById(2L)).thenReturn(Optional.of(new Trainer()));
        when(traineeService.getTraineeById(3L)).thenReturn(Optional.of(new Trainee()));
        when(trainingTypeService.getTrainingTypeByName("CARDIO")).thenReturn(Optional.of(new com.epamtask.model.TrainingTypeEntity()));

        trainingService.createTraining(
                trainingId, 2L, 3L, "Yoga", TrainingType.CARDIO, new Date(), "1h"
        );

        verify(databaseTrainingStorage).save(any(Training.class));
    }

    @Test
    void testCreateTraining_DuplicateId_ShouldThrowException() {
        when(databaseTrainingStorage.findById(1L)).thenReturn(Optional.of(new Training()));

        assertThrows(IllegalArgumentException.class, () ->
                trainingService.createTraining(1L, 2L, 3L, "Yoga",
                        TrainingType.CARDIO, new Date(), "1h"));
    }

    @Test
    void testGetTrainingById_Success() {
        Training training = new Training();
        when(databaseTrainingStorage.findById(1L)).thenReturn(Optional.of(training));

        Optional<Training> result = trainingService.getTrainingById(1L);

        assertTrue(result.isPresent());
        assertEquals(training, result.get());
    }

    @Test
    void testGetTrainingById_Null_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> trainingService.getTrainingById(null));
    }

    @Test
    void testGetAllTrainings_Success() {
        when(databaseTrainingStorage.findAll()).thenReturn(List.of(new Training()));

        List<Training> result = trainingService.getAllTrainings();

        assertEquals(1, result.size());
    }

    @Test
    void testGetAllTrainings_Empty_ShouldThrowException() {
        when(databaseTrainingStorage.findAll()).thenReturn(Collections.emptyList());

        assertThrows(IllegalStateException.class, () -> trainingService.getAllTrainings());
    }

    @Test
    void testGetTrainingsByTrainerId_Success() {
        when(databaseTrainingStorage.findByTrainerId(2L)).thenReturn(Map.of(1L, new Training()));

        Map<Long, Training> result = trainingService.getTrainingsByTrainerId(2L);

        assertEquals(1, result.size());
    }

    @Test
    void testGetTrainingsByTrainerId_Null_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> trainingService.getTrainingsByTrainerId(null));
    }

    @Test
    void testGetTrainingsByTraineeId_Success() {
        when(databaseTrainingStorage.findByTraineeId(3L)).thenReturn(Map.of(1L, new Training()));

        Map<Long, Training> result = trainingService.getTrainingsByTraineeId(3L);

        assertEquals(1, result.size());
    }

    @Test
    void testGetTrainingsByTraineeId_Null_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> trainingService.getTrainingsByTraineeId(null));
    }

    @Test
    void testGetTrainingsByTraineeUsernameAndCriteria_Success() {
        when(databaseTrainingStorage.findByTraineeUsernameAndCriteria(any(), any(), any(), any(), any())).thenReturn(List.of(new Training()));

        List<Training> result = trainingService.getTrainingsByTraineeUsernameAndCriteria("john", null, null, null, null);

        assertEquals(1, result.size());
    }

    @Test
    void testGetTrainingsByTrainerUsernameAndCriteria_Success() {
        when(databaseTrainingStorage.findByTrainerUsernameAndCriteria(any(), any(), any(), any())).thenReturn(List.of(new Training()));

        List<Training> result = trainingService.getTrainingsByTrainerUsernameAndCriteria("alex", null, null, null);

        assertEquals(1, result.size());
    }
}