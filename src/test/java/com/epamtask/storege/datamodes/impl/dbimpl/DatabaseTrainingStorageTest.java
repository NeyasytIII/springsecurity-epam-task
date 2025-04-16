package com.epamtask.storege.datamodes.impl.dbimpl;

import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import com.epamtask.repository.TrainingRepository;
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

class DatabaseTrainingStorageTest {

    private TrainingRepository trainingRepository;
    private DatabaseTrainingStorage storage;

    @BeforeEach
    void setUp() {
        trainingRepository = mock(TrainingRepository.class);
        storage = new DatabaseTrainingStorage(trainingRepository);
    }

    @Test
    void testFindById() {
        Training training = new Training();
        when(trainingRepository.findById(1L)).thenReturn(Optional.of(training));
        Optional<Training> result = storage.findById(1L);
        assertTrue(result.isPresent());
        verify(trainingRepository).findById(1L);
    }

    @Test
    void testFindAll() {
        when(trainingRepository.findAll()).thenReturn(List.of(new Training()));
        List<Training> result = storage.findAll();
        assertEquals(1, result.size());
        verify(trainingRepository).findAll();
    }

    @Test
    void testFindByTrainerId() {
        Trainer trainer = new Trainer();
        trainer.setTrainerId(1L);

        Training training = new Training();
        training.setTrainer(trainer);

        when(trainingRepository.findAll()).thenReturn(List.of(training));
        Map<Long, Training> result = storage.findByTrainerId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByTraineeId() {
        Trainee trainee = new Trainee();
        trainee.setTraineeId(1L);

        Training training = new Training();
        training.setTrainee(trainee);

        when(trainingRepository.findAll()).thenReturn(List.of(training));
        Map<Long, Training> result = storage.findByTraineeId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByTraineeUsernameAndCriteria() {
        List<Training> trainings = List.of(new Training());
        when(trainingRepository.findByTraineeUsernameAndCriteria("trainee", null, null, null, null)).thenReturn(trainings);
        List<Training> result = storage.findByTraineeUsernameAndCriteria("trainee", null, null, null, null);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByTrainerUsernameAndCriteria() {
        List<Training> trainings = List.of(new Training());
        when(trainingRepository.findByTrainerUsernameAndCriteria("trainer", null, null, null)).thenReturn(trainings);
        List<Training> result = storage.findByTrainerUsernameAndCriteria("trainer", null, null, null);
        assertEquals(1, result.size());
    }

    @Test
    void testSave() {
        Training training = new Training();
        storage.save(training);
        verify(trainingRepository).save(training);
    }

    @Test
    void testDeleteById() {
        storage.deleteById(5L);
        verify(trainingRepository).deleteById(5L);
    }
}