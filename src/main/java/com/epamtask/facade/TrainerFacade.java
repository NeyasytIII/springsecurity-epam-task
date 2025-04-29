package com.epamtask.facade;

import com.epamtask.dto.trainerdto.TrainerProfileResponseDto;
import com.epamtask.dto.trainerdto.TrainerShortDto;
import com.epamtask.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerFacade {
    void createTrainer(String firstName, String lastName, String specialization);
    void updateTrainer(Trainer trainer);
    Optional<Trainer> getTrainerById(Long id);
    Optional<Trainer> getTrainerByUsername(String username);
    List<Trainer> getAllTrainers();
    void activateUser(String username);
    void deactivateUser(String username);
    List<Trainer> getNotAssignedToTrainee(String traineeUsername);
    List<Trainer> getTrainersNotAssignedToTrainee(String traineeUsername);
    TrainerProfileResponseDto getTrainerProfile(String username);
    List<TrainerShortDto> getFreeTrainersNotAssignedByTrainings(String traineeUsername);
}