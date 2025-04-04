package com.epamtask.facade.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.facade.TrainerFacade;
import com.epamtask.model.Trainer;
import com.epamtask.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerFacadeImpl implements TrainerFacade {
    private final TrainerService trainerService;

    @Autowired
    public TrainerFacadeImpl(TrainerService trainerService) {
        this.trainerService = trainerService;
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
    public void updatePassword(String username, String newPassword) {
        trainerService.updatePassword(username, newPassword);
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

    @Override
    @Loggable
    public boolean verifyLogin(String username, String password) {
        return trainerService.verifyLogin(username, password);
    }

    @Override
    @Loggable
    public List<Trainer> getNotAssignedToTrainee(String traineeUsername) {
        return trainerService.getNotAssignedToTrainee(traineeUsername);
    }

    @Override
    @Loggable
    public List<Trainer> getTrainersNotAssignedToTrainee(String traineeUsername) {
        return trainerService.getTrainersNotAssignedToTrainee(traineeUsername);
    }

    @Override
    public void setInitialPassword(String username, String newPassword) {
        trainerService.setInitialPassword(username, newPassword);
    }

}