package com.epamtask.config;

import com.epamtask.service.impl.TrainingTypeInitServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TrainingTypePreloadConfig {

    private final TrainingTypeInitServiceImpl trainingTypeInitService;

    public TrainingTypePreloadConfig(TrainingTypeInitServiceImpl trainingTypeInitService) {
        this.trainingTypeInitService = trainingTypeInitService;
    }

    @PostConstruct
    public void preloadTrainingTypes() {
        trainingTypeInitService.initTrainingTypes();
    }
}