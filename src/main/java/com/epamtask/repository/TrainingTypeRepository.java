package com.epamtask.repository;

import com.epamtask.model.TrainingType;
import com.epamtask.model.TrainingTypeEntity;
import java.util.List;
import java.util.Optional;

public interface TrainingTypeRepository{
    List<TrainingTypeEntity> findAll();
    Optional<TrainingTypeEntity> findByName(String name);
    Optional<TrainingTypeEntity> findById(Long id);
    TrainingTypeEntity saveRaw(TrainingTypeEntity trainingTypeEntity);

}