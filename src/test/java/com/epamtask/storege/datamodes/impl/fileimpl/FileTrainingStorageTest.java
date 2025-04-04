package com.epamtask.storege.datamodes.impl.fileimpl;

import com.epamtask.dao.TrainingDAO;
import com.epamtask.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileTrainingStorageTest {

    private TrainingDAO trainingDAO;
    private FileTrainingStorage storage;

    @BeforeEach
    void setUp() {
        trainingDAO = mock(TrainingDAO.class);
        storage = new FileTrainingStorage(trainingDAO);
    }

    @Test
    void testSave() {
        Training training = new Training();
        training.setTrainingId(1L);
        storage.save(training);
        verify(trainingDAO).create(1L, training);
    }

    @Test
    void testFindById() {
        Training training = new Training();
        when(trainingDAO.findById(1L)).thenReturn(Optional.of(training));
        Optional<Training> result = storage.findById(1L);
        assertTrue(result.isPresent());
        verify(trainingDAO).findById(1L);
    }

    @Test
    void testFindAll() {
        Map<Long, Training> map = Map.of(1L, new Training());
        when(trainingDAO.getAll()).thenReturn(map);
        List<Training> result = storage.findAll();
        assertEquals(1, result.size());
        verify(trainingDAO).getAll();
    }

    @Test
    void testDeleteById() {
        storage.deleteById(1L);
        verify(trainingDAO).deleteById(1L);
    }

    @Test
    void testFindByTrainerId() {
        Map<Long, Training> map = Map.of(1L, new Training());
        when(trainingDAO.findByTrainerId(1L)).thenReturn(map);
        Map<Long, Training> result = storage.findByTrainerId(1L);
        assertEquals(1, result.size());
        verify(trainingDAO).findByTrainerId(1L);
    }

    @Test
    void testFindByTraineeId() {
        Map<Long, Training> map = Map.of(1L, new Training());
        when(trainingDAO.findByTraineeId(1L)).thenReturn(map);
        Map<Long, Training> result = storage.findByTraineeId(1L);
        assertEquals(1, result.size());
        verify(trainingDAO).findByTraineeId(1L);
    }

    @Test
    void testFindByTraineeUsernameAndCriteria() {
        List<Training> list = List.of(new Training());
        when(trainingDAO.findByTraineeUsernameAndCriteria("trainee", null, null, null, null)).thenReturn(list);
        List<Training> result = storage.findByTraineeUsernameAndCriteria("trainee", null, null, null, null);
        assertEquals(1, result.size());
        verify(trainingDAO).findByTraineeUsernameAndCriteria("trainee", null, null, null, null);
    }

    @Test
    void testFindByTrainerUsernameAndCriteria() {
        List<Training> list = List.of(new Training());
        when(trainingDAO.findByTrainerUsernameAndCriteria("trainer", null, null, null)).thenReturn(list);
        List<Training> result = storage.findByTrainerUsernameAndCriteria("trainer", null, null, null);
        assertEquals(1, result.size());
        verify(trainingDAO).findByTrainerUsernameAndCriteria("trainer", null, null, null);
    }
}