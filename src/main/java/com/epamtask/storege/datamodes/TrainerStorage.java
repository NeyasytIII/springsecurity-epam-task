package com.epamtask.storege.datamodes;

import com.epamtask.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerStorage {
    void save(Trainer trainer);
    Optional<Trainer> findById(Long id);
    Optional<Trainer> findByUsername(String username);
    List<Trainer> findAll();
    void deleteById(Long id);
    void activateUser(String username);
    void deactivateUser(String username);
    List<Trainer> findNotAssignedToTrainee(String traineeUsername);
}