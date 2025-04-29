package com.epamtask.facade.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.dto.trainerdto.TrainerProfileResponseDto;
import com.epamtask.dto.trainerdto.TrainerShortDto;
import com.epamtask.exception.NotFoundException;
import com.epamtask.facade.TrainerFacade;
import com.epamtask.mapper.TraineeMapper;
import com.epamtask.mapper.TrainerMapper;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import com.epamtask.service.TraineeService;
import com.epamtask.service.TrainerService;
import com.epamtask.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TrainerFacadeImpl implements TrainerFacade {

    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerMapper trainerMapper;
    private final TraineeMapper traineeMapper;

    @Autowired
    public TrainerFacadeImpl(
            TrainerService trainerService,
            TrainingService trainingService,
            TraineeService traineeService,
            TrainerMapper trainerMapper,
            TraineeMapper traineeMapper
    ) {
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.traineeService = traineeService;
        this.trainerMapper = trainerMapper;
        this.traineeMapper = traineeMapper;
    }

    @Loggable
    @Override
    public void createTrainer(String firstName, String lastName, String specialization) {
        trainerService.createTrainer(firstName, lastName, specialization);
    }

    @Loggable
    @Override
    public void updateTrainer(Trainer trainer) {
        trainerService.updateTrainer(trainer);
    }

    @Loggable
    @Override
    public Optional<Trainer> getTrainerById(Long id) {
        return trainerService.getTrainerById(id);
    }

    @Loggable
    @Override
    public Optional<Trainer> getTrainerByUsername(String username) {
        return trainerService.getTrainerByUsername(username);
    }

    @Loggable
    @Override
    public List<Trainer> getAllTrainers() {
        return trainerService.getAllTrainers();
    }

    @Loggable
    @Override
    public void activateUser(String username) {
        trainerService.activateUser(username);
    }

    @Loggable
    @Override
    public void deactivateUser(String username) {
        trainerService.deactivateUser(username);
    }

    @Loggable
    @Override
    public List<Trainer> getNotAssignedToTrainee(String traineeUsername) {
        return trainerService.getNotAssignedToTrainee(traineeUsername);
    }

    @Loggable
    @Override
    public List<Trainer> getTrainersNotAssignedToTrainee(String traineeUsername) {
        return trainerService.getTrainersNotAssignedToTrainee(traineeUsername);
    }

    @Loggable
    @Override
    public TrainerProfileResponseDto getTrainerProfile(String username) {
        Trainer trainer = getTrainerByUsername(username)
                .orElseThrow(() -> new NotFoundException("Trainer not found: " + username));

        List<Training> trainings = trainingService.getTrainingsByTrainerUsernameAndCriteria(username, null, null, null);
        Set<Trainee> trainees = trainings.stream()
                .map(Training::getTrainee)
                .collect(Collectors.toSet());

        TrainerProfileResponseDto dto = trainerMapper.toProfileDto(trainer);
        dto.setTrainees(traineeMapper.toShortDtoList(trainees));
        return dto;
    }

    @Loggable
    @Override
    public List<TrainerShortDto> getFreeTrainersNotAssignedByTrainings(String traineeUsername) {
        Trainee trainee = traineeService.getTraineeByUsername(traineeUsername)
                .orElseThrow(() -> new NotFoundException("Trainee not found: " + traineeUsername));

        List<Training> trainings = trainingService.getTrainingsByTraineeUsernameAndCriteria(
                traineeUsername, null, null, null, null);

        Set<Trainer> assignedTrainers = trainings.stream()
                .map(Training::getTrainer)
                .collect(Collectors.toSet());

        Set<Trainer> allActive = trainerService.getAllTrainers().stream()
                .filter(Trainer::isActive)
                .collect(Collectors.toSet());

        allActive.removeAll(assignedTrainers);

        return trainerMapper.toShortDtoList(allActive);
    }
}