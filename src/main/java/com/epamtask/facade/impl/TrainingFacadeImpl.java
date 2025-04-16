package com.epamtask.facade.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.dto.trainingdto.TrainingCreateRequestDto;
import com.epamtask.facade.TraineeFacade;
import com.epamtask.facade.TrainerFacade;
import com.epamtask.facade.TrainingFacade;
import com.epamtask.mapper.TrainingMapper;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import com.epamtask.model.TrainingType;
import com.epamtask.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TrainingFacadeImpl implements TrainingFacade {
    private final TrainingService trainingService;
    private final TraineeFacade traineeFacade;
    private final TrainerFacade trainerFacade;
    private final TrainingMapper trainingMapper;

    @Autowired
    public TrainingFacadeImpl(
            TrainingService trainingService,
            @Lazy TraineeFacade traineeFacade,
            TrainerFacade trainerFacade,
            TrainingMapper trainingMapper
    ) {
        this.trainingService = trainingService;
        this.traineeFacade = traineeFacade;
        this.trainerFacade = trainerFacade;
        this.trainingMapper = trainingMapper;
    }

    @Loggable
    @Override
    public void createTraining(Long trainerId, Long traineeId, String trainingName, TrainingType type, Date trainingDate, String trainingDuration) {
        trainingService.createTraining(trainerId, traineeId, trainingName, type, trainingDate, trainingDuration);
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

    @Loggable
    @Override
    public void createTrainingFromDto(TrainingCreateRequestDto dto) {
        Trainee trainee = traineeFacade.getTraineeByUsername(dto.getTraineeUsername())
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found: " + dto.getTraineeUsername()));
        Trainer trainer = trainerFacade.getTrainerByUsername(dto.getTrainerUsername())
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found: " + dto.getTrainerUsername()));
        Training training = trainingMapper.toEntity(dto, trainee, trainer);
        trainingService.createTraining(
                trainer.getTrainerId(),
                trainee.getTraineeId(),
                training.getTrainingName(),
                training.getType(),
                training.getTrainingDate(),
                training.getTrainingDuration()
        );
    }
}
