package com.epamtask.repository;

import com.epamtask.model.TrainingTypeEntity;
import com.epamtask.repository.impl.TrainingTypeRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainingTypeRepositoryImplTest {

    private TrainingTypeRepositoryImpl repository;
    private EntityManager entityManager;

    @BeforeEach
    void setUp() throws Exception {
        entityManager = mock(EntityManager.class);
        repository = new TrainingTypeRepositoryImpl();
        var field = TrainingTypeRepositoryImpl.class.getDeclaredField("entityManager");
        field.setAccessible(true);
        field.set(repository, entityManager);
    }

    @Test
    void testFindAll() {
        TypedQuery<TrainingTypeEntity> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(TrainingTypeEntity.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(new TrainingTypeEntity()));

        List<TrainingTypeEntity> result = repository.findAll();

        assertEquals(1, result.size());
        verify(entityManager).createQuery(anyString(), eq(TrainingTypeEntity.class));
    }

    @Test
    void testFindByName() {
        TypedQuery<TrainingTypeEntity> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(TrainingTypeEntity.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultStream()).thenReturn(List.of(new TrainingTypeEntity()).stream());

        Optional<TrainingTypeEntity> result = repository.findByName("YOGA");

        assertTrue(result.isPresent());
        verify(entityManager).createQuery(anyString(), eq(TrainingTypeEntity.class));
    }

    @Test
    void testFindById() {
        TrainingTypeEntity entity = new TrainingTypeEntity();
        when(entityManager.find(TrainingTypeEntity.class, 1L)).thenReturn(entity);

        Optional<TrainingTypeEntity> result = repository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
        verify(entityManager).find(TrainingTypeEntity.class, 1L);
    }

    @Test
    void testSaveRaw() {
        TrainingTypeEntity entity = new TrainingTypeEntity();
        TrainingTypeEntity result = repository.saveRaw(entity);

        verify(entityManager).persist(entity);
        assertEquals(entity, result);
    }
}