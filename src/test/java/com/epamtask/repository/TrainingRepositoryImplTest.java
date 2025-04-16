package com.epamtask.repository;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import com.epamtask.repository.impl.TrainingRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
class TrainingRepositoryImplTest {

    private TrainingRepositoryImpl repository;
    private EntityManager entityManager;

    @BeforeEach
    void setUp() throws Exception {
        entityManager = mock(EntityManager.class);
        repository = new TrainingRepositoryImpl();
        var field = TrainingRepositoryImpl.class.getDeclaredField("entityManager");
        field.setAccessible(true);
        field.set(repository, entityManager);
    }

    @Test
    void testSave() {
        Logger logger = LoggerFactory.getLogger(getClass());
        logger.debug("Creating Trainer and Trainee instances");

        Trainer trainer = new Trainer();
        trainer.setTrainerId(1L);
        logger.debug("Created Trainer: {}", trainer);

        Trainee trainee = new Trainee();
        trainee.setTraineeId(1L);
        logger.debug("Created Trainee: {}", trainee);

        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName("Yoga Training");
        training.setTrainingDate(new Date());
        training.setTrainingDuration("1 hour");
        logger.debug("Created Training: {}", training);

        Training result = repository.save(training);
        logger.debug("Saved Training: {}", result);

        verify(entityManager).persist(training);
        logger.debug("Persisted the training instance");
    }

    @Test
    void testUpdate() {
        Training training = new Training();
        repository.update(training);
        verify(entityManager).merge(training);
    }

    @Test
    void testFindById() {
        Training training = new Training();
        training.setTrainingName("Yoga Training");
        training.setTrainingDate(new Date());
        training.setTrainingDuration("1 hour");

        TypedQuery<Training> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.singletonList(training));

        Optional<Training> result = repository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(training, result.get());
    }

    @Test
    void testFindAll() {
        TypedQuery<Training> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(new Training()));
        List<Training> result = repository.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteById() {
        Training training = new Training();
        when(entityManager.find(Training.class, 1L)).thenReturn(training);
        repository.deleteById(1L);
        verify(entityManager).remove(training);
    }

    @Test
    void testDeleteAttached() {
        Training training = new Training();
        when(entityManager.contains(training)).thenReturn(true);
        repository.delete(training);
        verify(entityManager).remove(training);
    }

    @Test
    void testDeleteDetached() {
        Training training = new Training();
        when(entityManager.contains(training)).thenReturn(false);
        when(entityManager.merge(training)).thenReturn(training);
        repository.delete(training);
        verify(entityManager).remove(training);
    }

    @Test
    void testFindByTraineeUsernameAndCriteria() {
        TypedQuery<Training> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        List<Training> result = repository.findByTraineeUsernameAndCriteria("trainee", new Date(), new Date(), "trainer", "YOGA");
        assertNotNull(result);
    }

    @Test
    void testFindByTrainerUsernameAndCriteria() {
        TypedQuery<Training> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        List<Training> result = repository.findByTrainerUsernameAndCriteria("trainer", new Date(), new Date(), "trainee");
        assertNotNull(result);
    }
}