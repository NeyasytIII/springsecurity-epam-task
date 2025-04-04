package com.epamtask.service.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.service.AuthenticationService;
import com.epamtask.storege.datamodes.TraineeStorage;
import com.epamtask.storege.datamodes.TrainerStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TraineeStorage traineeStorage;
    private final TrainerStorage trainerStorage;

    public AuthenticationServiceImpl(
            @Qualifier("databaseTraineeStorage") TraineeStorage traineeStorage,
            @Qualifier("databaseTrainerStorage") TrainerStorage trainerStorage) {
        this.traineeStorage = traineeStorage;
        this.trainerStorage = trainerStorage;
    }

    @Loggable
    @Override
    public boolean authenticate(String username, String password) {
        boolean traineeValid = traineeStorage.findByUsername(username)
                .map(trainee -> trainee.getPassword().equals(password))
                .orElse(false);

        boolean trainerValid = trainerStorage.findByUsername(username)
                .map(trainer -> trainer.getPassword().equals(password))
                .orElse(false);

        return traineeValid || trainerValid;
    }

    public boolean checkCredentialsWithoutAuth(String username, String password) {
        boolean traineeMatch = traineeStorage.findByUsername(username)
                .map(trainee -> trainee.getPassword().equals(password))
                .orElse(false);

        boolean trainerMatch = trainerStorage.findByUsername(username)
                .map(trainer -> trainer.getPassword().equals(password))
                .orElse(false);

        return traineeMatch || trainerMatch;
    }

    @Loggable
    @Override
    public void updatePasswordWithoutAuth(String username, String newPassword) {
        if (trainerStorage.findByUsername(username).isPresent()) {
            trainerStorage.updatePassword(username, newPassword);
        } else if (traineeStorage.findByUsername(username).isPresent()) {
            traineeStorage.updatePassword(username, newPassword);
        }
    }
}