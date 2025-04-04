package com.epamtask.service.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.*;
import com.epamtask.service.*;
import com.epamtask.storege.datamodes.TrainingStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingStorage trainingStorage;
    private final TrainingTypeService trainingTypeService;
    private final TrainerService trainerService;
    private final TraineeService traineeService;

    public TrainingServiceImpl(
            @Value("${data.source}") String dataSource,
            @Qualifier("databaseTrainingStorage") TrainingStorage databaseStorage,
            @Qualifier("fileTrainingStorage") TrainingStorage fileStorage,
            TrainingTypeService trainingTypeService,
            TrainerService trainerService,
            TraineeService traineeService
    ) {
        this.trainingStorage = "DATABASE".equalsIgnoreCase(dataSource) ? databaseStorage : fileStorage;
        this.trainingTypeService = trainingTypeService;
        this.trainerService = trainerService;
        this.traineeService = traineeService;
    }

    @Override
    @Loggable
    public void createTraining(Long trainingId, Long trainerId, Long traineeId, String trainingName,
                               TrainingType type, Date trainingDate, String trainingDuration) {

        validateTrainingInput(trainingId, trainerId, traineeId, trainingName, type, trainingDate, trainingDuration);

        if (trainingStorage.findById(trainingId).isPresent()) {
            throw new IllegalArgumentException("Training with ID " + trainingId + " already exists");
        }

        Trainer trainer = trainerService.getTrainerById(trainerId)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found"));
        Trainee trainee = traineeService.getTraineeById(traineeId)
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found"));
        TrainingTypeEntity trainingTypeEntity = trainingTypeService
                .getTrainingTypeByName(type.name())
                .orElseThrow(() -> new IllegalArgumentException("Unknown training type: " + type));

        Training training = new Training();
        training.setTrainingId(trainingId);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingName(trainingName);
        training.setTrainingDate(trainingDate);
        training.setTrainingDuration(trainingDuration);
        training.setTrainingType(trainingTypeEntity);

        trainingStorage.save(training);
    }

    private void validateTrainingInput(Long trainingId, Long trainerId, Long traineeId, String trainingName,
                                       TrainingType type, Date trainingDate, String trainingDuration) {

        if (trainingId == null || trainingId <= 0)
            throw new IllegalArgumentException("Training ID must be positive");
        if (trainerId == null || trainerId <= 0)
            throw new IllegalArgumentException("Trainer ID must be positive");
        if (traineeId == null || traineeId <= 0)
            throw new IllegalArgumentException("Trainee ID must be positive");
        if (trainingName == null || trainingName.isBlank())
            throw new IllegalArgumentException("Training name is required");
        if (type == null)
            throw new IllegalArgumentException("Training type is required");
        if (trainingDate == null)
            throw new IllegalArgumentException("Training date is required");
        if (trainingDuration == null || trainingDuration.isBlank())
            throw new IllegalArgumentException("Training duration is required");
    }

    @Override
    @Loggable
    public Optional<Training> getTrainingById(Long trainingId) {
        if (trainingId == null) {
            throw new IllegalArgumentException("Training ID cannot be null");
        }
        return trainingStorage.findById(trainingId);
    }

    @Override
    @Loggable
    public Map<Long, Training> getTrainingsByTrainerId(Long trainerId) {
        if (trainerId == null) {
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }
        return trainingStorage.findByTrainerId(trainerId);
    }

    @Override
    @Loggable
    public Map<Long, Training> getTrainingsByTraineeId(Long traineeId) {
        if (traineeId == null) {
            throw new IllegalArgumentException("Trainee ID cannot be null");
        }
        return trainingStorage.findByTraineeId(traineeId);
    }

    @Override
    @Loggable
    public List<Training> getAllTrainings() {
        List<Training> trainings = trainingStorage.findAll();
        if (trainings == null || trainings.isEmpty()) {
            throw new IllegalStateException("No trainings found");
        }
        return trainings;
    }

    @Override
    @Loggable
    public List<Training> getTrainingsByTraineeUsernameAndCriteria(String traineeUsername, Date fromDate, Date toDate,
                                                                   String trainerName, String trainingType) {
        if (traineeUsername == null || traineeUsername.isBlank()) {
            throw new IllegalArgumentException("Trainee username is required");
        }
        return trainingStorage.findByTraineeUsernameAndCriteria(
                traineeUsername, fromDate, toDate, trainerName, trainingType);
    }

    @Override
    @Loggable
    public List<Training> getTrainingsByTrainerUsernameAndCriteria(String trainerUsername, Date fromDate, Date toDate,
                                                                   String traineeName) {
        if (trainerUsername == null || trainerUsername.isBlank()) {
            throw new IllegalArgumentException("Trainer username is required");
        }
        return trainingStorage.findByTrainerUsernameAndCriteria(
                trainerUsername, fromDate, toDate, traineeName);
    }
}