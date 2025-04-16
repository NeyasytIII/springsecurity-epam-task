package com.epamtask.service.impl;

import com.epamtask.aspect.annotation.Authenticated;
import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.config.ApplicationContextProvider;
import com.epamtask.exception.NotFoundException;
import com.epamtask.model.Trainer;
import com.epamtask.model.TrainingTypeEntity;
import com.epamtask.model.TrainingType;
import com.epamtask.service.TrainerService;
import com.epamtask.service.TrainingTypeService;
import com.epamtask.storege.datamodes.TraineeStorage;
import com.epamtask.storege.datamodes.TrainerStorage;
import com.epamtask.utils.PasswordGenerator;
import com.epamtask.utils.UserNameGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerStorage trainerStorage;
    private final UserNameGenerator userNameGenerator;
    private final TrainingTypeService trainingTypeService;

    public TrainerServiceImpl(
            @Value("${data.source}") String dataSource,
            @Qualifier("databaseTrainerStorage") TrainerStorage databaseStorage,
            @Qualifier("fileTrainerStorage") TrainerStorage fileStorage,
            UserNameGenerator userNameGenerator,
            TrainingTypeService trainingTypeService
    ) {
        this.trainerStorage = "DATABASE".equalsIgnoreCase(dataSource) ? databaseStorage : fileStorage;
        this.userNameGenerator = userNameGenerator;
        this.trainingTypeService = trainingTypeService;
    }

    @Loggable
    @Override
    public void createTrainer(String firstName, String lastName, String specialization) {
        if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank()) {
            throw new NotFoundException("Invalid input data");
        }
        String uniqueUsername = userNameGenerator.generateUserName(firstName, lastName);
        if (trainerStorage.findByUsername(uniqueUsername).isPresent()) {
            throw new NotFoundException("Trainer with username already exists: " + uniqueUsername);
        }
        TraineeStorage traineeStorage = ApplicationContextProvider.getBean(TraineeStorage.class);
        if (traineeStorage.findByUsername(uniqueUsername).isPresent()) {
            throw new NotFoundException("Username already taken by a trainee: " + uniqueUsername);
        }

        String password = PasswordGenerator.generatePassword();

        TrainingType type = TrainingType.valueOf(specialization);
        TrainingTypeEntity specializationType = trainingTypeService
                .getTrainingTypeByName(type.name())
                .orElseThrow(() -> new NotFoundException("Unknown specialization type: " + specialization));

        Trainer trainer = new Trainer(null, firstName, lastName, specialization, true);
        trainer.setUserName(uniqueUsername);
        trainer.setPassword(password);
        trainer.setSpecializationType(specializationType);

        trainerStorage.save(trainer);

        trainerStorage.findByUsername(uniqueUsername)
                .ifPresentOrElse(
                        t -> System.out.println(" Trainer saved and confirmed from DB: " + t),
                        () -> System.out.println(" Trainer NOT found in DB after save: " + uniqueUsername)
                );
    }

    @Loggable
    @Authenticated
    @Override
    public void updateTrainer(Trainer dto) {
        if (dto == null || dto.getUserName() == null || dto.getUserName().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        Trainer existing = trainerStorage.findByUsername(dto.getUserName())
                .orElseThrow(() -> new NotFoundException("Trainer not found: " + dto.getUserName()));
        if (!dto.getUserName().equals(existing.getUserName())) {
            existing.setUserName(dto.getUserName());
        }
        if (dto.getFirstName() != null) {
            existing.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            existing.setLastName(dto.getLastName());
        }
        if (dto.isActive() != existing.isActive()) {
            existing.setActive(dto.isActive());
        }
        if (dto.getSpecialization() != null) {
            TrainingTypeEntity tte = trainingTypeService
                    .getTrainingTypeByName(dto.getSpecialization())
                    .orElseThrow(() -> new NotFoundException("Unknown specialization: " + dto.getSpecialization()));
            existing.setSpecializationType(tte);
        }
        trainerStorage.save(existing);
    }

    @Loggable
    @Authenticated
    @Override
    public Optional<Trainer> getTrainerById(Long id) {
        return trainerStorage.findById(id);
    }

    @Loggable
    @Authenticated
    @Override
    public Optional<Trainer> getTrainerByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new NotFoundException("Username cannot be null or empty");
        }
        return trainerStorage.findByUsername(username);
    }

    @Loggable
    @Authenticated
    @Override
    public List<Trainer> getAllTrainers() {
        return trainerStorage.findAll();
    }

    @Loggable
    @Authenticated
    public void deleteTrainer(Long id) {
        if (id == null) {
            throw new NotFoundException("ID cannot be null");
        }
        trainerStorage.deleteById(id);
    }

    @Loggable
    @Override
    public void updatePassword(String username, String newPassword) {
        if (username == null || username.isBlank()) {
            throw new NotFoundException("Username cannot be null or empty");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new NotFoundException("New password cannot be null or empty");
        }
        Trainer trainer = trainerStorage.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Trainer not found: " + username));
        trainer.setPassword(newPassword);
        trainerStorage.save(trainer);
    }

    @Loggable
    @Authenticated
    @Override
    public void activateUser(String username) {
        if (username == null || username.isBlank()) {
            throw new NotFoundException("Username cannot be null or empty");
        }
        Trainer trainer = trainerStorage.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Trainer not found: " + username));
        trainer.setActive(true);
        trainerStorage.save(trainer);
    }

    @Loggable
    @Authenticated
    @Override
    public void deactivateUser(String username) {
        if (username == null || username.isBlank()) {
            throw new NotFoundException("Username cannot be null or empty");
        }
        Trainer trainer = trainerStorage.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Trainer not found: " + username));
        trainer.setActive(false);
        trainerStorage.save(trainer);
    }

    @Override
    @Loggable
    public boolean verifyLogin(String username, String password) {
        return trainerStorage.verifyLogin(username, password);
    }

    @Override
    @Loggable
    @Authenticated
    public List<Trainer> getNotAssignedToTrainee(String traineeUsername) {
        if (traineeUsername == null || traineeUsername.isBlank()) {
            throw new NotFoundException("Trainee username is required");
        }
        return trainerStorage.findNotAssignedToTrainee(traineeUsername);
    }

    @Override
    @Loggable
    @Authenticated
    public List<Trainer> getTrainersNotAssignedToTrainee(String traineeUsername) {
        return trainerStorage.findNotAssignedToTrainee(traineeUsername);
    }

    @Override
    @Loggable
    public void setInitialPassword(String username, String password) {
        trainerStorage.updatePassword(username, password);
    }
}
