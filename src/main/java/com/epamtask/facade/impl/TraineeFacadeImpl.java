package com.epamtask.facade.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.facade.TraineeFacade;
import com.epamtask.model.Trainee;
import com.epamtask.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TraineeFacadeImpl implements TraineeFacade {
    private final TraineeService traineeService;

    @Autowired
    public TraineeFacadeImpl(TraineeService traineeService) {
        this.traineeService = traineeService;
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
    public void updatePassword(String username, String newPassword) {
        traineeService.updatePassword(username, newPassword);
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
    @Override
    @Loggable
    public boolean verifyLogin(String username, String password) {
        return traineeService.verifyLogin(username, password);
    }
    @Override
    public void setInitialPassword(String username, String newPassword) {
        traineeService.setInitialPassword(username, newPassword);
    }
}