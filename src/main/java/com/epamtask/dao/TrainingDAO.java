package com.epamtask.dao;
import com.epamtask.model.Training;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TrainingDAO extends Dao<Long, Training> {
    Map<Long, Training> findByTrainerId(Long trainerId);
    Map<Long, Training> findByTraineeId(Long traineeId);
    Optional<Training> findById(Long trainingId);
    void deleteById(Long trainingId);
    List<Training> findByTraineeUsernameAndCriteria(String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType);

    List<Training> findByTrainerUsernameAndCriteria(String trainerUsername, Date fromDate, Date toDate, String traineeName);
}