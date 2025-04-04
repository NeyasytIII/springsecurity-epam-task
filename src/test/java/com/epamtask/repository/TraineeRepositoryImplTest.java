package com.epamtask.repository;

import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.repository.impl.TraineeRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Trainee> typedQuery;

    @Mock
    private TypedQuery<Trainer> trainerQuery;

    @InjectMocks
    private TraineeRepositoryImpl traineeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Trainee trainee = new Trainee();
        when(entityManager.merge(trainee)).thenReturn(trainee);
        Trainee result = traineeRepository.save(trainee);
        assertEquals(trainee, result);
    }

    @Test
    void testFindById() {
        Trainee trainee = new Trainee();
        when(entityManager.find(Trainee.class, 1L)).thenReturn(trainee);
        Optional<Trainee> result = traineeRepository.findById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void testFindAll() {
        when(entityManager.createQuery("SELECT t FROM Trainee t", Trainee.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(new Trainee()));
        List<Trainee> result = traineeRepository.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void testDelete() {
        Trainee trainee = new Trainee();
        when(entityManager.contains(trainee)).thenReturn(true);
        traineeRepository.delete(trainee);
        verify(entityManager).remove(trainee);
    }

    @Test
    void testDeleteById() {
        Trainee trainee = new Trainee();
        when(entityManager.find(Trainee.class, 1L)).thenReturn(trainee);
        traineeRepository.deleteById(1L);
        verify(entityManager).remove(trainee);
    }

    @Test
    void testFindByUsername() {
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("username"), anyString())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(new Trainee()));
        Optional<Trainee> result = traineeRepository.findByUsername("john");
        assertTrue(result.isPresent());
    }

    @Test
    void testExistsByUsernameAndPassword() {
        TypedQuery<Long> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(query);
        when(query.setParameter(eq("username"), eq("user"))).thenReturn(query);
        when(query.setParameter(eq("password"), eq("pass"))).thenReturn(query);
        when(query.getSingleResult()).thenReturn(1L);

        boolean result = traineeRepository.existsByUsernameAndPassword("user", "pass");

        assertTrue(result);
        verify(query).getSingleResult();
    }

    @Test
    void testUpdatePassword() {
        var q = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString())).thenReturn(q);
        when(q.setParameter(anyString(), any())).thenReturn(q);
        traineeRepository.updatePassword("u", "p");
        verify(q).executeUpdate();
    }

    @Test
    void testUpdateTraineeTrainersList() {
        Trainee trainee = new Trainee();
        trainee.setTrainers(Collections.newSetFromMap(new java.util.IdentityHashMap<>()));
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("username"), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(trainee));

        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(trainerQuery);
        when(trainerQuery.setParameter(eq("usernames"), any())).thenReturn(trainerQuery);
        when(trainerQuery.getResultList()).thenReturn(List.of(new Trainer()));

        traineeRepository.updateTraineeTrainersList("john", List.of("trainer1"));
        verify(entityManager).merge(trainee);
    }
}
