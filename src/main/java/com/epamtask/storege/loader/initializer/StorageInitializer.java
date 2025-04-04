package com.epamtask.storege.loader.initializer;

import com.epamtask.exception.InvalidDataException;
import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import com.epamtask.storege.loader.TraineeStorageLoader;
import com.epamtask.storege.loader.TrainerStorageLoader;
import com.epamtask.storege.loader.TrainingStorageLoader;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StorageInitializer {

    private final Map<Long, Trainee> traineeStorage;
    private final Map<Long, Trainer> trainerStorage;
    private final Map<Long, Training> trainingStorage;
    private final TraineeStorageLoader traineeLoader;
    private final TrainerStorageLoader trainerLoader;
    private final TrainingStorageLoader trainingLoader;

    public StorageInitializer(Map<Long, Trainee> traineeStorage,
                              Map<Long, Trainer> trainerStorage,
                              Map<Long, Training> trainingStorage,
                              TraineeStorageLoader traineeLoader,
                              TrainerStorageLoader trainerLoader,
                              TrainingStorageLoader trainingLoader) {
        this.traineeStorage = traineeStorage;
        this.trainerStorage = trainerStorage;
        this.trainingStorage = trainingStorage;
        this.traineeLoader = traineeLoader;
        this.trainerLoader = trainerLoader;
        this.trainingLoader = trainingLoader;
    }

    @PostConstruct
    @Loggable
    public void loadData() {
        System.out.println("Starting data loading...");
        try {
            traineeLoader.loadTrainees(traineeStorage);
            System.out.println("Trainees loaded: " + traineeStorage.size());

            trainerLoader.loadTrainers(trainerStorage);
            System.out.println("Trainers loaded: " + trainerStorage.size());

            trainingLoader.loadTrainings(trainingStorage, traineeStorage, trainerStorage);
            System.out.println("Trainings loaded: " + trainingStorage.size());

            System.out.println("All data loaded successfully.");

        } catch (InvalidDataException e) {
            System.err.println("Data loading error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error during storage initialization: " + e.getMessage());
        }
    }
}