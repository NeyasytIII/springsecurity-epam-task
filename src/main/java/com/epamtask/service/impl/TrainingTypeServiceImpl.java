package com.epamtask.service.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.TrainingTypeEntity;
import com.epamtask.service.TrainingTypeService;
import com.epamtask.storege.datamodes.TrainingTypeStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeStorage trainingTypeStorage;

    public TrainingTypeServiceImpl(
            @Value("${data.source}") String dataSource,
            @Qualifier("databaseTrainingTypeStorage") TrainingTypeStorage databaseStorage
    ) {
        this.trainingTypeStorage = "DATABASE".equalsIgnoreCase(dataSource) ? databaseStorage : null;
    }

    @Loggable
    @Override
    public List<TrainingTypeEntity> getAllTrainingTypes() {
        List<TrainingTypeEntity> all = trainingTypeStorage.findAll();
        if (all == null || all.isEmpty()) {
            throw new IllegalStateException("No training types found");
        }
        return all;
    }

    @Loggable
    @Override
    public Optional<TrainingTypeEntity> getTrainingTypeById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Training type ID must be positive");
        }
        return trainingTypeStorage.findById(id);
    }

    @Loggable
    @Override
    public Optional<TrainingTypeEntity> getTrainingTypeByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Training type name is required");
        }

        TrainingTypeEntity trainingTypeEntity = trainingTypeStorage.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Invalid training type: " + name));

        return Optional.of(trainingTypeEntity);
    }
}