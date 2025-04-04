package com.epamtask.dao.impl;

import com.epamtask.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TrainerDAOImplTest {

    private Map<Long, Trainer> trainerStorage;
    private TrainerDAOImpl trainerDAO;

    @BeforeEach
    void setUp() {
        trainerStorage = new HashMap<>();
        trainerDAO = new TrainerDAOImpl(trainerStorage);
    }

    @Test
    void testCreate() {
        Trainer trainer = new Trainer();
        trainer.setTrainerId(1L);
        trainer.setUserName("Anna.Doe");

        trainerDAO.create(1L, trainer);

        assertTrue(trainerStorage.containsKey(1L));
        assertEquals("Anna.Doe", trainerStorage.get(1L).getUserName());
    }

    @Test
    void testFindById() {
        Trainer trainer = new Trainer();
        trainer.setTrainerId(2L);
        trainer.setUserName("Sam.Smith");
        trainerStorage.put(2L, trainer);

        Optional<Trainer> result = trainerDAO.findById(2L);

        assertTrue(result.isPresent());
        assertEquals("Sam.Smith", result.get().getUserName());
    }

    @Test
    void testFindByUsername() {
        Trainer trainer = new Trainer();
        trainer.setTrainerId(3L);
        trainer.setUserName("Coach.John");
        trainerStorage.put(3L, trainer);

        Optional<Trainer> result = trainerDAO.findByUsername("Coach.John");

        assertTrue(result.isPresent());
        assertEquals(3L, result.get().getTrainerId());
    }

    @Test
    void testUpdate() {
        Trainer trainer = new Trainer();
        trainer.setTrainerId(4L);
        trainer.setUserName("Old.Name");
        trainerStorage.put(4L, trainer);

        trainer.setUserName("New.Name");
        trainerDAO.update(trainer);

        assertEquals("New.Name", trainerStorage.get(4L).getUserName());
    }

    @Test
    void testGetAllUsers() {
        Trainer t1 = new Trainer();
        t1.setTrainerId(5L);
        trainerStorage.put(5L, t1);

        Map<Long, Trainer> allUsers = trainerDAO.getAll();

        assertEquals(1, allUsers.size());
        assertTrue(allUsers.containsKey(5L));
    }
}