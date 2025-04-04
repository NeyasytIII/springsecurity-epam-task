package com.epamtask.storege.datamodes.impl.dbimpl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.Trainee;
import com.epamtask.repository.TraineeRepository;
import com.epamtask.storege.datamodes.TraineeStorage;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("databaseTraineeStorage")
public class DatabaseTraineeStorage implements TraineeStorage {

    private final TraineeRepository traineeRepository;
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseTraineeStorage.class);

    public DatabaseTraineeStorage(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
        LOG.info("DatabaseTraineeStorage initialized!");
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Trainee> findById(Long id) {
        return traineeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainee> findByUsername(String username) {
        return traineeRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> findAll() {
        return traineeRepository.findAll();
    }
    @Override
    @Transactional
    public void save(Trainee trainee) {
        traineeRepository.save(trainee);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        traineeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        traineeRepository.deleteByUsername(username);
    }

    @Override
    @Transactional
    public void updatePassword(String username, String newPassword) {
        traineeRepository.updatePassword(username, newPassword);
    }

    @Override
    @Transactional
    public void activateUser(String username) {
        traineeRepository.activateUser(username);
    }

    @Override
    @Transactional
    public void deactivateUser(String username) {
        traineeRepository.deactivateUser(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifyLogin(String username, String password) {
        return traineeRepository.existsByUsernameAndPassword(username, password);
    }
}