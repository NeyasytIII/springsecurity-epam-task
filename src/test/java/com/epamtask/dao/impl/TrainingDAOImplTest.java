package com.epamtask.dao.impl;

import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainingDAOImplTest {

    private Map<Long, Training> trainingStorage;
    private TrainingDAOImpl trainingDAO;

    @BeforeEach
    void setUp() {
        trainingStorage = new HashMap<>();
        trainingDAO = new TrainingDAOImpl(trainingStorage);
    }

    @Test
    void testCreate() {
        Trainer trainer = new Trainer();
        trainer.setTrainerId(1L);
        trainer.setUserName("John.Doe");

        Trainee trainee = new Trainee();
        trainee.setTraineeId(1L);
        trainee.setUserName("Alice.Brown");

        Training training = new Training();
        training.setTrainingId(1L);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingName("Yoga Class");
        training.setTrainingDate(new Date());
        training.setTrainingDuration("60");

        trainingDAO.create(1L, training);

        assertTrue(trainingStorage.containsKey(1L));
        assertEquals("Yoga Class", trainingStorage.get(1L).getTrainingName());
        assertEquals("John.Doe", trainingStorage.get(1L).getTrainer().getUserName());
        assertEquals("Alice.Brown", trainingStorage.get(1L).getTrainee().getUserName());
    }

    @Test
    void testFindById() {
        Trainer trainer = new Trainer();
        trainer.setTrainerId(1L);
        trainer.setUserName("John.Doe");

        Trainee trainee = new Trainee();
        trainee.setTraineeId(1L);
        trainee.setUserName("Alice.Brown");

        Training training = new Training();
        training.setTrainingId(1L);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingName("Yoga Class");
        training.setTrainingDate(new Date());
        training.setTrainingDuration("60");

        trainingStorage.put(1L, training);

        Optional<Training> result = trainingDAO.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Yoga Class", result.get().getTrainingName());
        assertEquals("John.Doe", result.get().getTrainer().getUserName());
        assertEquals("Alice.Brown", result.get().getTrainee().getUserName());
    }

    @Test
    void testFindByTrainerId() {
        Trainer trainer1 = new Trainer();
        trainer1.setTrainerId(1L);
        trainer1.setUserName("John.Doe");

        Trainer trainer2 = new Trainer();
        trainer2.setTrainerId(2L);
        trainer2.setUserName("Jane.Smith");

        Trainee trainee = new Trainee();
        trainee.setTraineeId(1L);
        trainee.setUserName("Alice.Brown");

        Training training1 = new Training();
        training1.setTrainingId(1L);
        training1.setTrainer(trainer1);
        training1.setTrainee(trainee);
        training1.setTrainingName("Yoga Class");
        training1.setTrainingDate(new Date());
        training1.setTrainingDuration("60");

        Training training2 = new Training();
        training2.setTrainingId(2L);
        training2.setTrainer(trainer2);
        training2.setTrainee(trainee);
        training2.setTrainingName("Pilates Class");
        training2.setTrainingDate(new Date());
        training2.setTrainingDuration("45");

        trainingStorage.put(1L, training1);
        trainingStorage.put(2L, training2);

        Map<Long, Training> result = trainingDAO.findByTrainerId(1L);

        assertEquals(1, result.size());
        assertTrue(result.containsKey(1L));
    }

    @Test
    void testFindByTraineeId() {
        Trainer trainer = new Trainer();
        trainer.setTrainerId(1L);
        trainer.setUserName("John.Doe");

        Trainee trainee1 = new Trainee();
        trainee1.setTraineeId(1L);
        trainee1.setUserName("Alice.Brown");

        Trainee trainee2 = new Trainee();
        trainee2.setTraineeId(2L);
        trainee2.setUserName("Bob.Johnson");

        Training training1 = new Training();
        training1.setTrainingId(1L);
        training1.setTrainer(trainer);
        training1.setTrainee(trainee1);
        training1.setTrainingName("Yoga Class");
        training1.setTrainingDate(new Date());
        training1.setTrainingDuration("60");

        Training training2 = new Training();
        training2.setTrainingId(2L);
        training2.setTrainer(trainer);
        training2.setTrainee(trainee2);
        training2.setTrainingName("Pilates Class");
        training2.setTrainingDate(new Date());
        training2.setTrainingDuration("45");

        trainingStorage.put(1L, training1);
        trainingStorage.put(2L, training2);

        Map<Long, Training> result = trainingDAO.findByTraineeId(1L);

        assertEquals(1, result.size());
        assertTrue(result.containsKey(1L));
    }

    @Test
    void testDeleteById() {
        Trainer trainer = new Trainer();
        trainer.setTrainerId(1L);
        trainer.setUserName("John.Doe");

        Trainee trainee = new Trainee();
        trainee.setTraineeId(1L);
        trainee.setUserName("Alice.Brown");

        Training training = new Training();
        training.setTrainingId(1L);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingName("Yoga Class");
        training.setTrainingDate(new Date());
        training.setTrainingDuration("60");

        trainingStorage.put(1L, training);

        trainingDAO.deleteById(1L);

        assertFalse(trainingStorage.containsKey(1L));
    }

    @Test
    void testFindByTraineeUsernameAndCriteria() {
        Trainer trainer = new Trainer();
        trainer.setTrainerId(1L);
        trainer.setUserName("John.Doe");

        Trainee trainee = new Trainee();
        trainee.setTraineeId(1L);
        trainee.setUserName("Alice.Brown");

        Training training1 = new Training();
        training1.setTrainingId(1L);
        training1.setTrainer(trainer);
        training1.setTrainee(trainee);
        training1.setTrainingName("Yoga Class");
        training1.setTrainingDate(new Date());
        training1.setTrainingDuration("60");

        trainingStorage.put(1L, training1);

        List<Training> result = trainingDAO.findByTraineeUsernameAndCriteria("Alice.Brown", null, null, null, null);

        assertEquals(1, result.size());
        assertTrue(result.contains(training1));
    }

    @Test
    void testFindByTrainerUsernameAndCriteria() {
        Trainer trainer = new Trainer();
        trainer.setTrainerId(1L);
        trainer.setUserName("John.Doe");

        Trainee trainee = new Trainee();
        trainee.setTraineeId(1L);
        trainee.setUserName("Alice.Brown");

        Training training1 = new Training();
        training1.setTrainingId(1L);
        training1.setTrainer(trainer);
        training1.setTrainee(trainee);
        training1.setTrainingName("Yoga Class");
        training1.setTrainingDate(new Date());
        training1.setTrainingDuration("60");

        trainingStorage.put(1L, training1);

        List<Training> result = trainingDAO.findByTrainerUsernameAndCriteria("John.Doe", null, null, null);

        assertEquals(1, result.size());
        assertTrue(result.contains(training1));
    }
}