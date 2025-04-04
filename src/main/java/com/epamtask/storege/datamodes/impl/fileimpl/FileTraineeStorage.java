package com.epamtask.storege.datamodes.impl.fileimpl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.dao.TraineeDAO;
import com.epamtask.model.Trainee;
import com.epamtask.storege.datamodes.TraineeStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("fileTraineeStorage")
public class FileTraineeStorage implements TraineeStorage {

    private final TraineeDAO traineeDAO;
    private static final Logger LOG = LoggerFactory.getLogger(FileTraineeStorage.class);

    public FileTraineeStorage(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
        LOG.info("⚡ FileTraineeStorage initialized!");
    }

    @Loggable
    @Override
    public void save(Trainee trainee) {
        traineeDAO.create(trainee.getTraineeId(), trainee);
    }

    @Loggable
    @Override
    public Optional<Trainee> findById(Long id) {
        return traineeDAO.findById(id);
    }

    @Loggable
    @Override
    public Optional<Trainee> findByUsername(String username) {
        return traineeDAO.findByUsername(username);
    }

    @Loggable
    @Override
    public List<Trainee> findAll() {
        return traineeDAO.getAll().values().stream().toList();
    }

    @Loggable
    @Override
    public void deleteById(Long id) {
        traineeDAO.deleteById(id);
    }

    @Loggable
    @Override
    public void deleteByUsername(String username) {
        traineeDAO.deleteByUsername(username);
    }
    @Loggable
    @Override
    public void updatePassword(String username, String newPassword) {
        traineeDAO.updatePassword(username, newPassword);
    }

    @Loggable
    @Override
    public void activateUser(String username) {
        traineeDAO.activateUser(username);
    }

    @Loggable
    @Override
    public void deactivateUser(String username) {
        traineeDAO.deactivateUser(username);
    }
    @Loggable
    @Override
    public boolean verifyLogin(String username, String password) {
        return traineeDAO.existsByUsernameAndPassword(username, password);
    }
}