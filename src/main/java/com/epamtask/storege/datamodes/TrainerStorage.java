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

    void update(Trainer t);

    boolean verifyLogin(String username, String password);

    List<Trainer> findNotAssignedToTrainee(String traineeUsername);
    void updatePassword(String username, String newPassword);



}