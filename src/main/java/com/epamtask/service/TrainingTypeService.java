package com.epamtask.service;
import com.epamtask.model.TrainingTypeEntity;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeService {
    List<TrainingTypeEntity> getAllTrainingTypes();
    Optional<TrainingTypeEntity> getTrainingTypeById(Long id);
    Optional<TrainingTypeEntity> getTrainingTypeByName(String name);

}