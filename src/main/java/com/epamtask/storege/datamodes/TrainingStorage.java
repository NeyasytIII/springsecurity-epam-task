package com.epamtask.storege.datamodes;

import com.epamtask.model.Training;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TrainingStorage {
    void save(Training training);

    Optional<Training> findById(Long id);

    List<Training> findAll();

    void deleteById(Long id);

    Map<Long, Training> findByTrainerId(Long trainerId);

    Map<Long, Training> findByTraineeId(Long traineeId);

    List<Training> findByTraineeUsernameAndCriteria(String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType);

    List<Training> findByTrainerUsernameAndCriteria(String trainerUsername, Date fromDate, Date toDate, String traineeName);

}