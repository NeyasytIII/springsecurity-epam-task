package com.epamtask.storege.loader.validation;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import com.epamtask.storege.loader.validation.common.DateValidator;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TrainingValidator {

    private final DateValidator dateValidator;

    public TrainingValidator(DateValidator dateValidator) {
        this.dateValidator = dateValidator;
    }

    @Loggable
    public List<String> validate(List<Training> trainings, Map<Long, Trainee> traineeStorage, Map<Long, Trainer> trainerStorage) {
        List<String> errors = new ArrayList<>();
        Set<Long> uniqueIds = new HashSet<>();

        for (Training training : trainings) {
            if (training.getTrainingId() == null || training.getTrainingId() <= 0) {
                errors.add("Invalid training ID for: " + training);
            }
            if (training.getTrainer() == null || training.getTrainer().getTrainerId() == null
                    || !trainerStorage.containsKey(training.getTrainer().getTrainerId())) {
                errors.add("Invalid trainer reference for: " + training);
            }
            if (training.getTrainee() == null || training.getTrainee().getTraineeId() == null
                    || !traineeStorage.containsKey(training.getTrainee().getTraineeId())) {
                errors.add("Invalid trainee reference for: " + training);
            }
            if (training.getTrainingName() == null || training.getTrainingName().isBlank()) {
                errors.add("Training name is invalid for: " + training);
            }
            if (training.getType() == null) {
                errors.add("Training type is invalid for: " + training);
            }
            if (!dateValidator.isDateValid(training.getTrainingDate())) {
                errors.add("Invalid or missing training date for: " + training);
            }
            if (training.getTrainingDuration() == null || training.getTrainingDuration().isBlank()) {
                errors.add("Training duration is invalid for: " + training);
            }
            if (!uniqueIds.add(training.getTrainingId())) {
                errors.add("Duplicate training ID: " + training.getTrainingId());
            }
        }
        return errors;
    }
}