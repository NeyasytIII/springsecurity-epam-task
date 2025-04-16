package com.epamtask.storege.datamodes.impl.dbimpl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.Training;
import com.epamtask.repository.TrainingRepository;
import com.epamtask.storege.datamodes.TrainingStorage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("databaseTrainingStorage")
public class DatabaseTrainingStorage implements TrainingStorage {

    private final TrainingRepository trainingRepository;

    public DatabaseTrainingStorage(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Training> findById(Long id) {
        return trainingRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> findAll() {
        return trainingRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Training> findByTrainerId(Long trainerId) {
        return trainingRepository.findAll().stream()
                .filter(training -> training.getTrainer().getTrainerId().equals(trainerId))
                .collect(Collectors.toMap(Training::getTrainingId, training -> training));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Training> findByTraineeId(Long traineeId) {
        return trainingRepository.findAll().stream()
                .filter(training -> training.getTrainee().getTraineeId().equals(traineeId))
                .collect(Collectors.toMap(Training::getTrainingId, training -> training));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> findByTraineeUsernameAndCriteria(String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType) {
        return trainingRepository.findByTraineeUsernameAndCriteria(traineeUsername, fromDate, toDate, trainerName, trainingType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> findByTrainerUsernameAndCriteria(String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        return trainingRepository.findByTrainerUsernameAndCriteria(trainerUsername, fromDate, toDate, traineeName);
    }

    @Override
    @Transactional
    public void save(Training training) {
        trainingRepository.save(training);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        trainingRepository.deleteById(id);
    }


    @Override
    @Transactional(readOnly = true)
    @Loggable
    public Optional<Training> findDuplicate(Long trainerId,
                                            Long traineeId,
                                            String trainingName,
                                            Date trainingDate) {
        return trainingRepository.findByFields(trainerId, traineeId, trainingName, trainingDate);
    }
}