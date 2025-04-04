package com.epamtask.dao.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.dao.TrainerDAO;
import com.epamtask.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class TrainerDAOImpl implements TrainerDAO {
    private final Map<Long, Trainer> trainerStorage;

    @Autowired
    public TrainerDAOImpl(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Loggable
    @Override
    public void create(Long id, Trainer trainer) {
        trainerStorage.put(id, trainer);
    }

    @Loggable
    @Override
    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable(trainerStorage.get(id));
    }

    @Loggable
    @Override
    public Optional<Trainer> findByUsername(String username) {
        return trainerStorage.values().stream()
                .filter(t -> t.getUserName().equals(username))
                .findFirst();
    }

    @Loggable
    @Override
    public void update(Trainer trainer) {
        trainerStorage.put(trainer.getTrainerId(), trainer);
    }

    @Loggable
    @Override
    public Map<Long, Trainer> getAll() {
        return trainerStorage;
    }

    @Loggable
    @Override
    public void deleteById(Long id) {
        trainerStorage.remove(id);
    }

    @Loggable
    @Override
    public void deleteByUsername(String username) {
        trainerStorage.values().removeIf(trainer -> trainer.getUserName().equals(username));
    }

    @Loggable
    @Override
    public void updatePassword(String username, String newPassword) {
        findByUsername(username).ifPresent(trainer -> trainer.setPassword(newPassword));
    }

    @Loggable
    @Override
    public void activateUser(String username) {
        findByUsername(username).ifPresent(trainer -> trainer.setActive(true));
    }

    @Loggable
    @Override
    public void deactivateUser(String username) {
        findByUsername(username).ifPresent(trainer -> trainer.setActive(false));
    }
}