package com.epamtask.config;

import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

@PropertySource({"classpath:application.properties", "classpath:path.properties"})
@Configuration
public class StorageConfig {

    private final Environment environment;


    public StorageConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public Map<Long, Trainee> traineeStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Trainer> trainerStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Training> trainingStorage() {
        return new HashMap<>();
    }
    @Bean
    public String traineeFilePath() {
        return environment.getProperty("storage.file.trainee");
    }

    @Bean
    public String trainerFilePath() {
        return environment.getProperty("storage.file.trainer");
    }

    @Bean
    public String trainingFilePath() {
        return environment.getProperty("storage.file.training");
    }
}