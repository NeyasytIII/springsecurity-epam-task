package com.epamtask.facade.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.dto.traineedto.TraineeProfileResponseDto;
import com.epamtask.exception.NotFoundException;
import com.epamtask.facade.TraineeFacade;
import com.epamtask.facade.TrainingFacade;
import com.epamtask.mapper.TraineeMapper;
import com.epamtask.mapper.TrainerMapper;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import com.epamtask.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TraineeFacadeImpl implements TraineeFacade {

    private final TraineeService traineeService;
    private final TrainingFacade trainingFacade;
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;

    @Autowired
    public TraineeFacadeImpl(
            TraineeService traineeService,
            TrainingFacade trainingFacade,
            TraineeMapper traineeMapper,
            TrainerMapper trainerMapper
    ) {
        this.traineeService = traineeService;
        this.trainingFacade = trainingFacade;
        this.traineeMapper = traineeMapper;
        this.trainerMapper = trainerMapper;
    }

    @Loggable
    @Override
    public void createTrainee(String firstName, String lastName, String address, Date birthdayDate) {
        traineeService.createTrainee(firstName, lastName, address, birthdayDate);
    }

    @Loggable
    @Override
    public void updateTrainee(Trainee trainee) {
        traineeService.updateTrainee(trainee);
    }

    @Loggable
    @Override
    public void deleteTrainee(String username) {
        traineeService.deleteTraineeByUsername(username);
    }

    @Loggable
    @Override
    public Optional<Trainee> getTraineeById(Long id) {
        return traineeService.getTraineeById(id);
    }

    @Loggable
    @Override
    public Optional<Trainee> getTraineeByUsername(String username) {
        return traineeService.getTraineeByUsername(username);
    }

    @Loggable
    @Override
    public List<Trainee> getAllTrainees() {
        return traineeService.getAllTrainees();
    }

    @Loggable
    @Override
    public void activateTrainee(String username) {
        traineeService.activateUser(username);
    }

    @Loggable
    @Override
    public void deactivateTrainee(String username) {
        traineeService.deactivateUser(username);
    }

    @Loggable
    @Override
    public void assignTrainersToTrainee(String traineeUsername, List<String> trainerUsernames) {
        traineeService.assignTrainersToTrainee(traineeUsername, trainerUsernames);
    }

    @Loggable
    @Override
    public TraineeProfileResponseDto getTraineeProfile(String username) {
        Trainee trainee = getTraineeByUsername(username)
                .orElseThrow(() -> new NotFoundException("Trainee not found: " + username));

        List<Training> trainings = trainingFacade.getTrainingsByTraineeUsernameAndCriteria(
                username, null, null, null, null);

        Set<Trainer> trainers = trainings.stream()
                .map(Training::getTrainer)
                .collect(Collectors.toSet());

        TraineeProfileResponseDto dto = traineeMapper.toProfileDto(trainee);
        dto.setTrainers(trainerMapper.toShortDtoList(trainers));
        return dto;
    }
}