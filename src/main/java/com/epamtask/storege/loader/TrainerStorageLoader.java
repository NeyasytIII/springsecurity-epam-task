package com.epamtask.storege.loader;

import com.epamtask.exception.InvalidDataException;
import com.epamtask.model.Trainer;
import com.epamtask.storege.loader.filereaders.JsonFileReader;
import com.epamtask.storege.loader.validation.uservalidation.TrainerValidator;
import com.epamtask.storege.loader.validation.common.FileValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TrainerStorageLoader {

    private final ResourceLoader resourceLoader;
    private final String trainerFilePath;
    private final JsonFileReader trainerFileReader;
    private final TrainerValidator trainerValidator;

    public TrainerStorageLoader(ResourceLoader resourceLoader,
                                @Qualifier("trainerFilePath") String trainerFilePath,
                                JsonFileReader trainerFileReader,
                                TrainerValidator trainerValidator) {
        this.resourceLoader = resourceLoader;
        this.trainerFilePath = trainerFilePath;
        this.trainerFileReader = trainerFileReader;
        this.trainerValidator = trainerValidator;
    }

    public void loadTrainers(Map<Long, Trainer> trainerStorage) {
        Resource resource = resourceLoader.getResource("classpath:" + trainerFilePath);

        if (!FileValidator.isFileValid(resource, trainerFilePath)) {
            throw new InvalidDataException("Invalid trainer file: " + trainerFilePath);
        }

        List<Trainer> trainers = trainerFileReader.readFromFile(resource, Trainer.class);

        List<String> validationErrors = trainerValidator.validate(trainers);
        if (!validationErrors.isEmpty()) {
            throw new InvalidDataException("Errors validating trainers:\n" + String.join("\n", validationErrors));
        }

        List<String> usernameErrors = verifyUsernames(trainers);
        if (!usernameErrors.isEmpty()) {
            throw new InvalidDataException("Errors verifying trainer usernames:\n" + String.join("\n", usernameErrors));
        }

        trainers.forEach(trainer -> trainerStorage.put(trainer.getTrainerId(), trainer));
    }

    private List<String> verifyUsernames(List<Trainer> trainers) {
        List<String> errors = new ArrayList<>();
        for (Trainer t : trainers) {
            String expected = capitalize(t.getFirstName()) + "." + capitalize(t.getLastName());
            if (!expected.equalsIgnoreCase(t.getUserName())) {
                errors.add("UserName mismatch for user ID " + t.getTrainerId() +
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