package com.epamtask.service.impl;

import com.epamtask.aspect.annotation.Authenticated;
import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.config.ApplicationContextProvider;
import com.epamtask.exception.NotFoundException;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.service.TraineeService;
import com.epamtask.storege.datamodes.TraineeStorage;
import com.epamtask.storege.datamodes.TrainerStorage;
import com.epamtask.utils.PasswordGenerator;
import com.epamtask.utils.UserNameGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {

    private final TraineeStorage traineeStorage;
    private final TrainerStorage trainerStorage;
    private final UserNameGenerator userNameGenerator;

    public TraineeServiceImpl(@Value("${data.source}") String dataSource,
                              @Qualifier("databaseTraineeStorage") TraineeStorage databaseStorage,
                              @Qualifier("fileTraineeStorage") TraineeStorage fileStorage,
                              @Qualifier("databaseTrainerStorage") TrainerStorage trainerStorage,
                              UserNameGenerator userNameGenerator) {
        this.traineeStorage = "DATABASE".equalsIgnoreCase(dataSource) ? databaseStorage : fileStorage;
        this.trainerStorage = trainerStorage;
        this.userNameGenerator = userNameGenerator;
    }

    @Loggable
    @Override
    public void createTrainee(String firstName, String lastName, String address, Date birthdayDate) {
        if (firstName == null || firstName.isBlank() ||
                lastName == null || lastName.isBlank() ||
                address == null || address.isBlank() ||
                birthdayDate == null) {
            throw new IllegalArgumentException("Invalid input data");
        }

        String uniqueUsername = userNameGenerator.generateUserName(firstName, lastName);
        if (traineeStorage.findByUsername(uniqueUsername).isPresent()) {
            throw new IllegalArgumentException("Trainee with username already exists: " + uniqueUsername);
        }
        TrainerStorage trainerStorage = ApplicationContextProvider.getBean(TrainerStorage.class);
        if (trainerStorage.findByUsername(uniqueUsername).isPresent()) {
            throw new IllegalArgumentException("Username already taken by a trainer: " + uniqueUsername);
        }

        String password = PasswordGenerator.generatePassword();
        Trainee trainee = new Trainee(null, firstName, lastName, address, birthdayDate, true);
        trainee.setUserName(uniqueUsername);
        trainee.setPassword(password);
        traineeStorage.save(trainee);
    }

    @Loggable
    @Authenticated
    @Override
    public void updateTrainee(Trainee dto) {
        if (dto == null || dto.getUserName() == null || dto.getUserName().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        Trainee existing = traineeStorage.findByUsername(dto.getUserName())
                .orElseThrow(() -> new NotFoundException("Trainee not found: " + dto.getUserName()));
        if (!dto.getUserName().equals(existing.getUserName())) {
            existing.setUserName(dto.getUserName());
        }
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setAddress(dto.getAddress());
        existing.setBirthdayDate(dto.getBirthdayDate());
        existing.setActive(dto.isActive());
        traineeStorage.save(existing);
    }

    @Loggable
    @Authenticated
    @Override
    public void deleteTrainee(Long id) {
        if (id == null) {
            throw new NotFoundException("ID cannot be null");
        }
        traineeStorage.deleteById(id);
    }

    @Loggable
    @Authenticated
    @Override
    public void deleteTraineeByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (traineeStorage.findByUsername(username).isEmpty()) {
            throw new NotFoundException("Trainee not found: " + username);
        }
        traineeStorage.deleteByUsername(username);
    }

    @Loggable
    @Authenticated
    @Override
    public Optional<Trainee> getTraineeById(Long id) {
        return traineeStorage.findById(id);
    }

    @Override
    @Loggable
    @Authenticated
    public Optional<Trainee> getTraineeByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return Optional.ofNullable(
                traineeStorage.findByUsername(username)
                        .orElseThrow(() -> new NotFoundException("Trainee not found: " + username))
        );
    }

    @Loggable
    @Authenticated
    @Override
    public List<Trainee> getAllTrainees() {
        List<Trainee> result = traineeStorage.findAll();
        if (result == null || result.isEmpty()) {
            throw new NotFoundException("No trainees found");
        }
        return result;
    }

    @Loggable
    @Authenticated
    @Override
    public void updatePassword(String username, String newPassword) {
        if (username == null || username.isBlank() || newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        traineeStorage.findByUsername(username).ifPresentOrElse(
                trainee -> {
                    trainee.setPassword(newPassword);
                    traineeStorage.save(trainee);
                },
                () -> { throw new NotFoundException("Trainee not found: " + username); }
        );
    }

    @Loggable
    @Authenticated
    @Override
    public void activateUser(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        traineeStorage.findByUsername(username).ifPresent(
                trainee -> {
                    trainee.setActive(true);
                    traineeStorage.save(trainee);
                }
        );
    }

    @Loggable
    @Authenticated
    @Override
    public void deactivateUser(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        traineeStorage.findByUsername(username).ifPresent(
                trainee -> {
                    trainee.setActive(false);
                    traineeStorage.save(trainee);
                }
        );
    }

    @Loggable
    @Authenticated
    @Override
    public void assignTrainersToTrainee(String traineeUsername, List<String> trainerUsernames) {
        if (trainerUsernames == null || trainerUsernames.isEmpty()) {
            return;
        }
        Trainee trainee = traineeStorage.findByUsername(traineeUsername)
                .orElseThrow(() -> new NotFoundException("Trainee not found: " + traineeUsername));
        List<Trainer> trainers = trainerUsernames.stream()
                .map(trainerStorage::findByUsername)
                .flatMap(Optional::stream)
                .filter(trainer -> !trainee.getTrainers().contains(trainer))
                .toList();
        if (!trainers.isEmpty()) {
            trainee.getTrainers().clear();
            trainee.getTrainers().addAll(trainers);
            traineeStorage.save(trainee);
        }
    }

    @Loggable
    @Authenticated
    @Override
    public boolean verifyLogin(String username, String password) {
        return traineeStorage.verifyLogin(username, password);
    }

    @Override
    public void setInitialPassword(String username, String newPassword) {
        traineeStorage.updatePassword(username, newPassword);
    }
}
