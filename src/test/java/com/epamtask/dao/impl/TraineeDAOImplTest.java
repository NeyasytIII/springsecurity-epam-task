package com.epamtask.dao.impl;

import com.epamtask.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TraineeDAOImplTest {

    private Map<Long, Trainee> traineeStorage;
    private TraineeDAOImpl traineeDAO;

    @BeforeEach
    void setUp() {
        traineeStorage = new HashMap<>();
        traineeDAO = new TraineeDAOImpl(traineeStorage);
    }

    @Test
    void testCreate() {
        Trainee trainee = new Trainee();
        trainee.setTraineeId(1L);
        trainee.setUserName("John.Doe");

        traineeDAO.create(1L, trainee);

        assertTrue(traineeStorage.containsKey(1L));
        assertEquals("John.Doe", traineeStorage.get(1L).getUserName());
    }

    @Test
    void testUpdate() {
        Trainee trainee = new Trainee();
        trainee.setTraineeId(2L);
        trainee.setUserName("Jane.Doe");
        traineeStorage.put(2L, trainee);

        trainee.setUserName("Jane.Updated");
        traineeDAO.update(trainee);

        assertEquals("Jane.Updated", traineeStorage.get(2L).getUserName());
    }

    @Test
    void testFindById() {
        Trainee trainee = new Trainee();
        trainee.setTraineeId(3L);
        trainee.setUserName("Bob.Smith");
        traineeStorage.put(3L, trainee);

        Optional<Trainee> result = traineeDAO.findById(3L);

        assertTrue(result.isPresent());
        assertEquals("Bob.Smith", result.get().getUserName());
    }

    @Test
    void testDeleteById() {
        Trainee trainee = new Trainee();
        trainee.setTraineeId(4L);
        trainee.setUserName("Alice.Doe");
        traineeStorage.put(4L, trainee);

        traineeDAO.deleteById(4L);

        assertFalse(traineeStorage.containsKey(4L));
    }

    @Test
    void testFindByUsername() {
        Trainee trainee = new Trainee();
        trainee.setTraineeId(5L);
        trainee.setUserName("Max.Power");
        traineeStorage.put(5L, trainee);

        Optional<Trainee> result = traineeDAO.findByUsername("Max.Power");

        assertTrue(result.isPresent());
        assertEquals(5L, result.get().getTraineeId());
    }

    @Test
    void testGetAllUsers() {
        Trainee t1 = new Trainee();
        t1.setTraineeId(10L);
        traineeStorage.put(10L, t1);

        Map<Long, Trainee> allUsers = traineeDAO.getAll();

        assertEquals(1, allUsers.size());
        assertTrue(allUsers.containsKey(10L));
    }
}