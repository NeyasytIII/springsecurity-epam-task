package com.epamtask.dao.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.dao.TrainingDAO;
import com.epamtask.model.Training;
import com.epamtask.model.Trainer;
import com.epamtask.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TrainingDAOImpl implements TrainingDAO {

    private final Map<Long, Training> trainingStorage;

    @Autowired
    public TrainingDAOImpl(Map<Long, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    @Loggable
    @Override
    public void create(Long id, Training training) {
        trainingStorage.put(id, training);
    }

    @Loggable
    @Override
    public Optional<Training> findById(Long id) {
        return Optional.ofNullable(trainingStorage.get(id));
    }

    @Loggable
    @Override
    public Map<Long, Training> findByTrainerId(Long trainerId) {
        return trainingStorage.entrySet().stream()
                .filter(entry -> entry.getValue().getTrainer().getTrainerId().equals(trainerId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Loggable
    @Override
    public void deleteById(Long id) {
        trainingStorage.remove(id);
    }

    @Loggable
    @Override
    public Map<Long, Training> findByTraineeId(Long traineeId) {
        return trainingStorage.entrySet().stream()
                .filter(entry -> entry.getValue().getTrainee().getTraineeId().equals(traineeId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Loggable
    @Override
    public Map<Long, Training> getAll() {
        return trainingStorage;
    }

    @Loggable
    @Override
    public List<Training> findByTraineeUsernameAndCriteria(String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType) {
        return trainingStorage.values().stream()
                .filter(training -> training.getTrainee().getUserName().equals(traineeUsername))
                .filter(training -> (fromDate == null || !training.getTrainingDate().before(fromDate)))
                .filter(training -> (toDate == null || !training.getTrainingDate().after(toDate)))
                .filter(training -> (trainerName == null || training.getTrainer().getUserName().equals(trainerName)))
                .filter(training -> (trainingType == null || training.getType().name().equals(trainingType)))
                .toList();
    }

    @Loggable
    @Override
    public List<Training> findByTrainerUsernameAndCriteria(String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        return trainingStorage.values().stream()
                .filter(training -> training.getTrainer().getUserName().equals(trainerUsername))
                .filter(training -> (fromDate == null || !training.getTrainingDate().before(fromDate)))
                .filter(training -> (toDate == null || !training.getTrainingDate().after(toDate)))
                .filter(training -> (traineeName == null || training.getTrainee().getUserName().equals(traineeName)))
                .toList();
    }
}