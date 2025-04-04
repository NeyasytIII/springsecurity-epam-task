package com.epamtask.storege.datamodes.impl.fileimpl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.dao.TrainingDAO;
import com.epamtask.model.Training;
import com.epamtask.storege.datamodes.TrainingStorage;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component("fileTrainingStorage")
public class FileTrainingStorage implements TrainingStorage {

    private final TrainingDAO trainingDAO;

    public FileTrainingStorage(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Loggable
    @Override
    public void save(Training training) {
        trainingDAO.create(training.getTrainingId(), training);
    }

    @Loggable
    @Override
    public Optional<Training> findById(Long id) {
        return trainingDAO.findById(id);
    }

    @Loggable
    @Override
    public List<Training> findAll() {
        return trainingDAO.getAll().values().stream().toList();
    }

    @Loggable
    @Override
    public void deleteById(Long id) {
        trainingDAO.deleteById(id);
    }

    @Loggable
    @Override
    public Map<Long, Training> findByTrainerId(Long trainerId) {
        return trainingDAO.findByTrainerId(trainerId);
    }

    @Loggable
    @Override
    public Map<Long, Training> findByTraineeId(Long traineeId) {
        return trainingDAO.findByTraineeId(traineeId);
    }

    @Loggable
    @Override
    public List<Training> findByTraineeUsernameAndCriteria(String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType) {
        return trainingDAO.findByTraineeUsernameAndCriteria(traineeUsername, fromDate, toDate, trainerName, trainingType);
    }

    @Loggable
    @Override
    public List<Training> findByTrainerUsernameAndCriteria(String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        return trainingDAO.findByTrainerUsernameAndCriteria(trainerUsername, fromDate, toDate, traineeName);
    }
}