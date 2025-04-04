package com.epamtask.repository.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.TrainingType;
import com.epamtask.model.TrainingTypeEntity;
import com.epamtask.repository.TrainingTypeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainingTypeRepositoryImpl implements TrainingTypeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Loggable
    @Override
    public List<TrainingTypeEntity> findAll() {
        return entityManager.createQuery("SELECT t FROM TrainingTypeEntity t", TrainingTypeEntity.class)
                .getResultList();
    }

    @Loggable
    @Override
    public Optional<TrainingTypeEntity> findByName(String name) {
        try {
            TrainingType type = TrainingType.valueOf(name.toUpperCase());
            return entityManager.createQuery("SELECT t FROM TrainingTypeEntity t WHERE t.type = :type", TrainingTypeEntity.class)
                    .setParameter("type", type)
                    .getResultStream()
                    .findFirst();
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Loggable
    @Override
    public Optional<TrainingTypeEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(TrainingTypeEntity.class, id));
    }

    @Loggable
    public TrainingTypeEntity saveRaw(TrainingTypeEntity trainingTypeEntity) {
        entityManager.persist(trainingTypeEntity);
        return trainingTypeEntity;
    }

}