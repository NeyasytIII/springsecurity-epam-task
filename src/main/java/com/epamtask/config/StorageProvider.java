package com.epamtask.config;

import com.epamtask.storege.datamodes.TraineeStorage;
import com.epamtask.storege.datamodes.TrainerStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StorageProvider {

    private final TraineeStorage traineeStorage;
    private final TrainerStorage trainerStorage;

    public StorageProvider(
            @Value("${data.source}") String dataSource,
            @Qualifier("databaseTraineeStorage") TraineeStorage databaseTraineeStorage,
            @Qualifier("fileTraineeStorage") TraineeStorage fileTraineeStorage,
            @Qualifier("databaseTrainerStorage") TrainerStorage databaseTrainerStorage,
            @Qualifier("fileTrainerStorage") TrainerStorage fileTrainerStorage
    ) {
        this.traineeStorage = "DATABASE".equalsIgnoreCase(dataSource) ? databaseTraineeStorage : fileTraineeStorage;
        this.trainerStorage = "DATABASE".equalsIgnoreCase(dataSource) ? databaseTrainerStorage : fileTrainerStorage;
    }

    public TraineeStorage getTraineeStorage() {
        return traineeStorage;
    }

    public TrainerStorage getTrainerStorage() {
        return trainerStorage;
    }
}
