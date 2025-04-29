package com.epamtask.storege.loader.initializer;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.UserCredentials;
import com.epamtask.repository.TraineeRepository;
import com.epamtask.repository.TrainerRepository;
import com.epamtask.repository.UserCredentialsRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class UserCredentialsInit implements ApplicationListener<ApplicationReadyEvent> {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordEncoder passwordEncoder;

    public UserCredentialsInit(
            TraineeRepository traineeRepository,
            TrainerRepository trainerRepository,
            UserCredentialsRepository userCredentialsRepository,
            PasswordEncoder passwordEncoder) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.userCredentialsRepository = userCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Loggable
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<UserCredentials> traineeCredentials = traineeRepository.findAll().stream()
                .map(trainee -> {
                    UserCredentials creds = new UserCredentials();
                    creds.setUsername(trainee.getUserName());
                    creds.setHashedPassword(passwordEncoder.encode(trainee.getPassword()));
                    creds.setRole("TRAINEE");
                    return creds;
                })
                .toList();

        List<UserCredentials> trainerCredentials = trainerRepository.findAll().stream()
                .map(trainer -> {
                    UserCredentials creds = new UserCredentials();
                    creds.setUsername(trainer.getUserName());
                    creds.setHashedPassword(passwordEncoder.encode(trainer.getPassword()));
                    creds.setRole("TRAINER");
                    return creds;
                })
                .toList();

        Stream.concat(traineeCredentials.stream(), trainerCredentials.stream())
                .forEach(userCredentialsRepository::save);
    }
}