package com.epamtask.storege.datamodes;
import com.epamtask.model.TrainingTypeEntity;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeStorage {
    List<TrainingTypeEntity> findAll();
    Optional<TrainingTypeEntity> findById(Long id);
    Optional<TrainingTypeEntity> findByName(String name);

}