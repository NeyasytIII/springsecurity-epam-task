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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TraineeRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Trainee> traineeQuery;

    @Mock
    private TypedQuery<Trainer> trainerQuery;

    @InjectMocks
    private TraineeRepositoryImpl traineeRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save() {
        Trainee t = new Trainee();
        when(entityManager.merge(t)).thenReturn(t);
        Trainee saved = traineeRepository.save(t);
        assertEquals(t, saved);
    }

    @Test
    void findById() {
        Trainee t = new Trainee();
        when(entityManager.find(Trainee.class, 1L)).thenReturn(t);
        Optional<Trainee> res = traineeRepository.findById(1L);
        assertTrue(res.isPresent());
    }

    @Test
    void findAll() {
        when(entityManager.createQuery("SELECT t FROM Trainee t", Trainee.class)).thenReturn(traineeQuery);
        when(traineeQuery.getResultList()).thenReturn(List.of(new Trainee()));
        List<Trainee> list = traineeRepository.findAll();
        assertEquals(1, list.size());
    }

    @Test
    void delete() {
        Trainee t = new Trainee();
        when(entityManager.contains(t)).thenReturn(true);
        traineeRepository.delete(t);
        verify(entityManager).remove(t);
    }

    @Test
    void deleteById() {
        Trainee t = new Trainee();
        when(entityManager.find(Trainee.class, 1L)).thenReturn(t);
        traineeRepository.deleteById(1L);
        verify(entityManager).remove(t);
    }

    @Test
    void findByUsername() {
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(traineeQuery);
        when(traineeQuery.setParameter("username", "john")).thenReturn(traineeQuery);
        when(traineeQuery.getResultStream()).thenReturn(Stream.of(new Trainee()));
        Optional<Trainee> result = traineeRepository.findByUsername("john");
        assertTrue(result.isPresent());
    }

    @Test
    void activateUser() {
        var q = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString())).thenReturn(q);
        when(q.setParameter(anyString(), any())).thenReturn(q);
        traineeRepository.activateUser("user1");
        verify(q).executeUpdate();
    }

    @Test
    void deactivateUser() {
        var q = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString())).thenReturn(q);
        when(q.setParameter(anyString(), any())).thenReturn(q);
        traineeRepository.deactivateUser("user1");
        verify(q).executeUpdate();
    }

    @Test
    void deleteByUsername() {
        var q = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString())).thenReturn(q);
        when(q.setParameter(anyString(), any())).thenReturn(q);
        traineeRepository.deleteByUsername("user1");
        verify(q).executeUpdate();
    }

    @Test
    void update() {
        Trainee t = new Trainee();
        traineeRepository.update(t);
        verify(entityManager).merge(t);
    }

    @Test
    void updateTraineeTrainersList() {
        Trainee trainee = new Trainee();
        trainee.setTrainers(new HashSet<>());
        when(entityManager.createQuery(contains("Trainee t"), eq(Trainee.class))).thenReturn(traineeQuery);
        when(traineeQuery.setParameter("username", "john")).thenReturn(traineeQuery);
        when(traineeQuery.getResultStream()).thenReturn(Stream.of(trainee));

        when(entityManager.createQuery(contains("Trainer t"), eq(Trainer.class))).thenReturn(trainerQuery);
        when(trainerQuery.setParameter(eq("usernames"), any())).thenReturn(trainerQuery);
        when(trainerQuery.getResultList()).thenReturn(List.of(new Trainer()));

        traineeRepository.updateTraineeTrainersList("john", List.of("tr1"));

        verify(entityManager).merge(trainee);
    }
}