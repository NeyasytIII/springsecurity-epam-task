package com.epamtask.repository;

import com.epamtask.model.Trainer;
import com.epamtask.repository.impl.TrainerRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerRepositoryImplTest {

    private TrainerRepositoryImpl repository;
    private EntityManager entityManager;

    @BeforeEach
    void setUp() throws Exception {
        entityManager = mock(EntityManager.class);
        repository = new TrainerRepositoryImpl();

        Field emField = TrainerRepositoryImpl.class.getDeclaredField("entityManager");
        emField.setAccessible(true);
        emField.set(repository, entityManager);
    }

    @Test
    void testSaveNewTrainer() {
        Trainer trainer = new Trainer();
        trainer.setTrainerId(null);
        repository.save(trainer);
        verify(entityManager).persist(trainer);
    }

    @Test
    void testSaveExistingTrainer() {
        Trainer trainer = new Trainer();
        trainer.setTrainerId(1L);
        when(entityManager.merge(trainer)).thenReturn(trainer);
        Trainer result = repository.save(trainer);
        assertEquals(trainer, result);
        verify(entityManager).merge(trainer);
    }

    @Test
    void testFindById() {
        Trainer trainer = new Trainer();
        when(entityManager.find(Trainer.class, 1L)).thenReturn(trainer);
        Optional<Trainer> result = repository.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
    }

    @Test
    void testFindAll() {
        TypedQuery<Trainer> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(new Trainer()));
        List<Trainer> result = repository.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteById() {
        Trainer trainer = new Trainer();
        when(entityManager.find(Trainer.class, 1L)).thenReturn(trainer);
        repository.deleteById(1L);
        verify(entityManager).remove(trainer);
    }

    @Test
    void testDeleteAttached() {
        Trainer trainer = new Trainer();
        when(entityManager.contains(trainer)).thenReturn(true);
        repository.delete(trainer);
        verify(entityManager).remove(trainer);
    }

    @Test
    void testDeleteDetached() {
        Trainer trainer = new Trainer();
        when(entityManager.contains(trainer)).thenReturn(false);
        when(entityManager.merge(trainer)).thenReturn(trainer);
        repository.delete(trainer);
        verify(entityManager).remove(trainer);
    }

    @Test
    void testFindByUsername() {
        TypedQuery<Trainer> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultStream()).thenReturn(List.of(new Trainer()).stream());
        Optional<Trainer> result = repository.findByUsername("test");
        assertTrue(result.isPresent());
    }

    @Test
    void testDeleteByUsername() {
        Query query = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        repository.deleteByUsername("test");
        verify(query).executeUpdate();
    }

    @Test
    void testFindNotAssignedToTrainee() {
        TypedQuery<Trainer> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());
        List<Trainer> result = repository.findNotAssignedToTrainee("test");
        assertTrue(result.isEmpty());
    }

    @Test
    void testExistsByUsernameAndPassword() {
        TypedQuery<Long> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(query);
        when(query.setParameter(eq("username"), any())).thenReturn(query);
        when(query.setParameter(eq("password"), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(1L);
        boolean result = repository.existsByUsernameAndPassword("user", "pass");
        assertTrue(result);
    }

    @Test
    void testUpdatePassword() {
        Query query = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        repository.updatePassword("user", "newpass");
        verify(query).executeUpdate();
    }

    @Test
    void testActivateUser() {
        Query query = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        repository.activateUser("user");
        verify(query).executeUpdate();
    }

    @Test
    void testDeactivateUser() {
        Query query = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        repository.deactivateUser("user");
        verify(query).executeUpdate();
    }

    @Test
    void testUpdate() {
        Trainer trainer = new Trainer();
        repository.update(trainer);
        verify(entityManager).merge(trainer);
    }
}