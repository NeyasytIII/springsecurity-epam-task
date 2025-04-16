package com.epamtask.facade.impl;

import com.epamtask.model.Training;
import com.epamtask.model.TrainingType;
import com.epamtask.model.TrainingTypeEntity;
import com.epamtask.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainingFacadeImplTest {

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingFacadeImpl trainingFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTraining() {
        trainingFacade.createTraining(1L, 2L, "Yoga Relax", TrainingType.YOGA, new Date(), "45");
        verify(trainingService).createTraining(eq(1L), eq(2L), eq("Yoga Relax"), eq(TrainingType.YOGA), any(), eq("45"));
    }

    @Test
    void testGetTrainingById() {
        Training training = new Training(10L, "Yoga Relax", new TrainingTypeEntity(TrainingType.YOGA), new Date(), "45");
        when(trainingService.getTrainingById(10L)).thenReturn(Optional.of(training));

        Optional<Training> result = trainingFacade.getTrainingById(10L);

        assertTrue(result.isPresent());
        assertEquals(10L, result.get().getTrainingId());
        verify(trainingService).getTrainingById(10L);
    }

    @Test
    void testGetAllTrainings() {
        List<Training> trainings = List.of(new Training(10L, "Yoga Relax", new TrainingTypeEntity(TrainingType.YOGA), new Date(), "45"));
        when(trainingService.getAllTrainings()).thenReturn(trainings);

        List<Training> result = trainingFacade.getAllTrainings();

        assertEquals(1, result.size());
        verify(trainingService).getAllTrainings();
    }

    @Test
    void testGetTrainingsByTrainerId() {
        Map<Long, Training> trainings = Map.of(
                10L, new Training(10L, "Yoga Relax", new TrainingTypeEntity(TrainingType.YOGA), new Date(), "45")
        );
        when(trainingService.getTrainingsByTrainerId(2L)).thenReturn(trainings);

        Map<Long, Training> result = trainingFacade.getTrainingsByTrainerId(2L);

        assertEquals(1, result.size());
        verify(trainingService).getTrainingsByTrainerId(2L);
    }

    @Test
    void testGetTrainingsByTraineeId() {
        Map<Long, Training> trainings = Map.of(
                10L, new Training(10L, "Yoga Relax", new TrainingTypeEntity(TrainingType.YOGA), new Date(), "45")
        );
        when(trainingService.getTrainingsByTraineeId(1L)).thenReturn(trainings);

        Map<Long, Training> result = trainingFacade.getTrainingsByTraineeId(1L);

        assertEquals(1, result.size());
        verify(trainingService).getTrainingsByTraineeId(1L);
    }

    @Test
    void testGetTrainingsByTraineeUsernameAndCriteria() {
        List<Training> trainings = List.of(new Training(10L, "Yoga Relax", new TrainingTypeEntity(TrainingType.YOGA), new Date(), "45"));
        when(trainingService.getTrainingsByTraineeUsernameAndCriteria(any(), any(), any(), any(), any()))
                .thenReturn(trainings);

        List<Training> result = trainingFacade.getTrainingsByTraineeUsernameAndCriteria("alice", new Date(), new Date(), "john", "YOGA");

        assertEquals(1, result.size());
        verify(trainingService).getTrainingsByTraineeUsernameAndCriteria(any(), any(), any(), any(), any());
    }

    @Test
    void testGetTrainingsByTrainerUsernameAndCriteria() {
        List<Training> trainings = List.of(new Training(10L, "Yoga Relax", new TrainingTypeEntity(TrainingType.YOGA), new Date(), "45"));
        when(trainingService.getTrainingsByTrainerUsernameAndCriteria(any(), any(), any(), any()))
                .thenReturn(trainings);

        List<Training> result = trainingFacade.getTrainingsByTrainerUsernameAndCriteria("john", new Date(), new Date(), "alice");

        assertEquals(1, result.size());
        verify(trainingService).getTrainingsByTrainerUsernameAndCriteria(any(), any(), any(), any());
    }
}