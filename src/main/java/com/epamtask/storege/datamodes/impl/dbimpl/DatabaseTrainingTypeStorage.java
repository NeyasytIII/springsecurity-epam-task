package com.epamtask.storege.datamodes.impl.dbimpl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.TrainingType;
import com.epamtask.model.TrainingTypeEntity;
import com.epamtask.repository.TrainingTypeRepository;
import com.epamtask.storege.datamodes.TrainingTypeStorage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component("databaseTrainingTypeStorage")
public class DatabaseTrainingTypeStorage implements TrainingTypeStorage {

    private final TrainingTypeRepository trainingTypeRepository;

    public DatabaseTrainingTypeStorage(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingTypeEntity> findAll() {
        return trainingTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainingTypeEntity> findById(Long id) {
        return trainingTypeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainingTypeEntity> findByName(String name) {
        return trainingTypeRepository.findByName(name);
    }

    @Transactional
    public void saveRaw(TrainingTypeEntity trainingTypeEntity) {
        trainingTypeRepository.saveRaw(trainingTypeEntity);
    }
}