package com.epamtask.dao.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.dao.TraineeDAO;
import com.epamtask.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class TraineeDAOImpl implements TraineeDAO {
    private final Map<Long, Trainee> traineeStorage;

    @Autowired
    public TraineeDAOImpl(Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    @Loggable
    @Override
    public void create(Long id, Trainee trainee) {
        traineeStorage.put(id, trainee);
    }

    @Loggable
    @Override
    public void update(Trainee trainee) {
        traineeStorage.put(trainee.getTraineeId(), trainee);
    }

    @Loggable
    @Override
    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable(traineeStorage.get(id));
    }

    @Loggable
    @Override
    public void deleteById(Long id) {
        traineeStorage.remove(id);
    }

    @Loggable
    @Override
    public Optional<Trainee> findByUsername(String username) {
        return traineeStorage.values().stream()
                .filter(t -> t.getUserName().equals(username))
                .findFirst();
    }

    @Loggable
    @Override
    public Map<Long, Trainee> getAll() {
        return traineeStorage;
    }
    @Loggable
    @Override
    public void deleteByUsername(String username) {
        traineeStorage.values().removeIf(trainee -> trainee.getUserName().equals(username));
    }

    @Loggable
    @Override
    public void updatePassword(String username, String newPassword) {
        findByUsername(username).ifPresent(trainee -> trainee.setPassword(newPassword));
    }

    @Loggable
    @Override
    public void activateUser(String username) {
        findByUsername(username).ifPresent(trainee -> trainee.setActive(true));
    }

    @Loggable
    @Override
    public void deactivateUser(String username) {
        findByUsername(username).ifPresent(trainee -> trainee.setActive(false));
    }

    @Override
    public boolean verifyLogin(String username, String password) {
        return false;
    }

    @Override
    public boolean existsByUsernameAndPassword(String username, String password) {
        return getAll().values().stream()
                .anyMatch(t -> t.getUserName().equals(username) && t.getPassword().equals(password));
    }
}