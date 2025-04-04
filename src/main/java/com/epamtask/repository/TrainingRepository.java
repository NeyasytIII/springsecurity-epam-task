package com.epamtask.repository;

import com.epamtask.model.Training;
import java.util.Date;
import java.util.List;

public interface TrainingRepository extends BaseRepository<Training, Long> {
    List<Training> findByTraineeUsernameAndCriteria(
            String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType
    );

    List<Training> findByTrainerUsernameAndCriteria(
            String trainerUsername, Date fromDate, Date toDate, String traineeName
    );
}