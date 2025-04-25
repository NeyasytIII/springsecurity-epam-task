package com.epamtask.service.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.service.AuthenticationService;
import com.epamtask.storege.datamodes.TraineeStorage;
import com.epamtask.storege.datamodes.TrainerStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TraineeStorage traineeStorage;
    private final TrainerStorage trainerStorage;

    public AuthenticationServiceImpl(
            @Value("${data.source}") String dataSource,
            @Qualifier("databaseTraineeStorage") TraineeStorage dbTrainee,
            @Qualifier("fileTraineeStorage")     TraineeStorage fileTrainee,
            @Qualifier("databaseTrainerStorage") TrainerStorage dbTrainer,
            @Qualifier("fileTrainerStorage")     TrainerStorage fileTrainer
    ) {
        this.traineeStorage = "DATABASE".equalsIgnoreCase(dataSource) ? dbTrainee : fileTrainee;
        this.trainerStorage = "DATABASE".equalsIgnoreCase(dataSource) ? dbTrainer : fileTrainer;
    }

    @Loggable
    @Override
    public boolean authenticate(String username, String password) {
        var t = traineeStorage.findByUsername(username);
        System.out.println("Trainee found? " + t.isPresent());
        t.ifPresent(trainee -> System.out.println("Stored password: " + trainee.getPassword() + ", Given: " + password));

        var r = trainerStorage.findByUsername(username);
        System.out.println("Trainer found? " + r.isPresent());
        r.ifPresent(trainer -> System.out.println("Stored password: " + trainer.getPassword() + ", Given: " + password));

        boolean traineeValid = t.map(trainee -> trainee.getPassword().equals(password)).orElse(false);
        boolean trainerValid = r.map(trainer -> trainer.getPassword().equals(password)).orElse(false);

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