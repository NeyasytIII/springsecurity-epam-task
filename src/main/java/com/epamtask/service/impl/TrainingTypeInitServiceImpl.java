package com.epamtask.service.impl;

import com.epamtask.model.TrainingType;
import com.epamtask.model.TrainingTypeEntity;
import com.epamtask.repository.TrainingTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainingTypeInitServiceImpl {

    private final TrainingTypeRepository trainingTypeRepository;

    public TrainingTypeInitServiceImpl(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Transactional
    public void initTrainingTypes() {
        for (TrainingType type : TrainingType.values()) {
            trainingTypeRepository.findByName(type.name())
                    .orElseGet(() -> trainingTypeRepository.saveRaw(new TrainingTypeEntity(type)));
        }
    }
}