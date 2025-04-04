package com.epamtask.storege.loader.initializer;


import com.epamtask.service.impl.TrainingTypeInitServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class TrainingTypeInitializer {

    private final TrainingTypeInitServiceImpl trainingTypeInitService;

    public TrainingTypeInitializer(TrainingTypeInitServiceImpl trainingTypeInitService) {
        this.trainingTypeInitService = trainingTypeInitService;
    }

    @PostConstruct
    public void init() {
        trainingTypeInitService.initTrainingTypes();
    }
}