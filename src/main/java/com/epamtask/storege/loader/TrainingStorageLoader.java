package com.epamtask.storege.loader;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.exception.InvalidDataException;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import com.epamtask.storege.loader.filereaders.JsonFileReader;
import com.epamtask.storege.loader.validation.TrainingValidator;
import com.epamtask.storege.loader.validation.common.FileValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TrainingStorageLoader {

    private final ResourceLoader resourceLoader;
    private final String trainingFilePath;
    private final JsonFileReader trainingFileReader;
    private final TrainingValidator trainingValidator;

    public TrainingStorageLoader(ResourceLoader resourceLoader,
                                 @Qualifier("trainingFilePath") String trainingFilePath,
                                 JsonFileReader trainingFileReader,
                                 TrainingValidator trainingValidator) {
        this.resourceLoader = resourceLoader;
        this.trainingFilePath = trainingFilePath;
        this.trainingFileReader = trainingFileReader;
        this.trainingValidator = trainingValidator;
    }

    @Loggable
    public void loadTrainings(Map<Long, Training> trainingStorage,
                              Map<Long, Trainee> traineeStorage,
                              Map<Long, Trainer> trainerStorage) {
        Resource resource = resourceLoader.getResource("classpath:" + trainingFilePath);

        if (!FileValidator.isFileValid(resource, trainingFilePath)) {
            throw new InvalidDataException("Invalid training file: " + trainingFilePath);
        }

        List<Training> trainings = trainingFileReader.readFromFile(resource, Training.class);
        List<String> validationErrors = trainingValidator.validate(trainings, traineeStorage, trainerStorage);

        if (!validationErrors.isEmpty()) {
            throw new InvalidDataException("Errors validating trainings:\n" + String.join("\n", validationErrors));
        }

        trainings.forEach(training -> trainingStorage.put(training.getTrainingId(), training));
    }
}