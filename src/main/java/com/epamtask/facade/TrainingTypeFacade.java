package com.epamtask.facade;

import com.epamtask.model.TrainingTypeEntity;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeFacade {
    List<TrainingTypeEntity> getAllTrainingTypes();
    Optional<TrainingTypeEntity> getTrainingTypeById(Long id);
    Optional<TrainingTypeEntity> getTrainingTypeByName(String name);

}