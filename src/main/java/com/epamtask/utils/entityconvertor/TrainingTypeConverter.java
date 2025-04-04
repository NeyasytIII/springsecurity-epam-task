package com.epamtask.utils.entityconvertor;

import com.epamtask.model.TrainingType;
import com.epamtask.model.TrainingTypeEntity;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Converter(autoApply = true)
public class TrainingTypeConverter implements AttributeConverter<TrainingType, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long convertToDatabaseColumn(TrainingType trainingType) {
        TrainingTypeEntity entity = entityManager.createQuery(
                        "SELECT t FROM TrainingTypeEntity t WHERE t.type = :type", TrainingTypeEntity.class)
                .setParameter("type", trainingType)
                .getSingleResult();
        return entity.getId();
    }

    @Override
    public TrainingType convertToEntityAttribute(Long id) {
        return entityManager.find(TrainingTypeEntity.class, id).getType();
    }
}