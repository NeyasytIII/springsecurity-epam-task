package com.epamtask.storege.loader;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.exception.InvalidDataException;
import com.epamtask.model.Trainee;
import com.epamtask.storege.loader.filereaders.JsonFileReader;
import com.epamtask.storege.loader.validation.common.FileValidator;
import com.epamtask.storege.loader.validation.uservalidation.TraineeValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@Component
public class TraineeStorageLoader {

    private final ResourceLoader resourceLoader;
    private final String traineeFilePath;
    private final FileValidator fileValidator;
    private final JsonFileReader traineeFileReader;
    private final TraineeValidator traineeValidator;

    public TraineeStorageLoader(ResourceLoader resourceLoader,
                                @Qualifier("traineeFilePath") String traineeFilePath,
                                FileValidator fileValidator,
                                JsonFileReader traineeFileReader,
                                TraineeValidator traineeValidator) {
        this.resourceLoader = resourceLoader;
        this.traineeFilePath = traineeFilePath;
        this.fileValidator = fileValidator;
        this.traineeFileReader = traineeFileReader;
        this.traineeValidator = traineeValidator;
    }

    @Loggable
    public void loadTrainees(Map<Long, Trainee> traineeStorage) {
        Resource resource = resourceLoader.getResource("classpath:" + traineeFilePath);

        if (!fileValidator.isFileValid(resource, traineeFilePath)) {
            throw new InvalidDataException("Invalid trainee file: " + traineeFilePath);
        }

        List<Trainee> trainees = traineeFileReader.readFromFile(resource, Trainee.class);

        List<String> validationErrors = traineeValidator.validate(trainees);
        if (!validationErrors.isEmpty()) {
            throw new InvalidDataException("Validation errors: " + String.join("; ", validationErrors));
        }

        List<String> userNameErrors = verifyUsernames(trainees);
        if (!userNameErrors.isEmpty()) {
            throw new InvalidDataException("UserName verification errors: " + String.join("; ", userNameErrors));
        }

        trainees.forEach(trainee -> traineeStorage.put(trainee.getTraineeId(), trainee));
    }

    private List<String> verifyUsernames(List<Trainee> trainees) {
        List<String> errors = new ArrayList<>();
        for (Trainee t : trainees) {
            String expected = capitalize(t.getFirstName()) + "." + capitalize(t.getLastName());
            if (!expected.equalsIgnoreCase(t.getUserName())) {
                errors.add("UserName mismatch for user ID " + t.getTraineeId() +
                        ": expected " + expected + " but found " + t.getUserName());
            }
        }
        return errors;
    }

    private String capitalize(String str) {
        return str == null || str.isBlank()
                ? ""
                : str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}