package com.epamtask.facade.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.facade.TrainingFacade;
import com.epamtask.model.Training;
import com.epamtask.model.TrainingType;
import com.epamtask.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TrainingFacadeImpl implements TrainingFacade {
    private final TrainingService trainingService;

    @Autowired
    public TrainingFacadeImpl(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @Loggable
    @Override
    public void createTraining(Long trainingId, Long traineeId, Long trainerId, String trainingName, TrainingType type, Date trainingDate, String trainingDuration) {
        trainingService.createTraining(trainingId, traineeId, trainerId, trainingName, type, trainingDate, trainingDuration);
    }

    @Loggable
    @Override
    public Optional<Training> getTrainingById(Long trainingId) {
        return trainingService.getTrainingById(trainingId);
    }

    @Loggable
    @Override
    public Map<Long, Training> getTrainingsByTrainerId(Long trainerId) {
        return trainingService.getTrainingsByTrainerId(trainerId);
    }

    @Loggable
    @Override
    public Map<Long, Training> getTrainingsByTraineeId(Long traineeId) {
        return trainingService.getTrainingsByTraineeId(traineeId);
    }

    @Loggable
    @Override
    public List<Training> getAllTrainings() {
        return trainingService.getAllTrainings();
    }

    @Loggable
    @Override
    public List<Training> getTrainingsByTraineeUsernameAndCriteria(
            String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType) {
        return trainingService.getTrainingsByTraineeUsernameAndCriteria(traineeUsername, fromDate, toDate, trainerName, trainingType);
    }

    @Loggable
    @Override
    public List<Training> getTrainingsByTrainerUsernameAndCriteria(
            String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        return trainingService.getTrainingsByTrainerUsernameAndCriteria(trainerUsername, fromDate, toDate, traineeName);
    }
}