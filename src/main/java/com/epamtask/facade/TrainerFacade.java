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
    void updatePassword(String username, String newPassword);
    void activateUser(String username);
    void deactivateUser(String username);

    boolean verifyLogin(String username, String password);
    List<Trainer> getNotAssignedToTrainee(String traineeUsername);
    List<Trainer> getTrainersNotAssignedToTrainee(String traineeUsername);
    void setInitialPassword(String username, String newPassword);
    TrainerProfileResponseDto getTrainerProfile(String username);
    public List<TrainerShortDto> getFreeTrainersNotAssignedByTrainings(String traineeUsername);

}