package com.epamtask.storege.datamodes;

import com.epamtask.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeStorage {
    void save(Trainee trainee);
    Optional<Trainee> findById(Long id);
    Optional<Trainee> findByUsername(String username);
    List<Trainee> findAll();
    void deleteById(Long id);

    void deleteByUsername(String username);
    void updatePassword(String username, String newPassword);
    void activateUser(String username);
    void deactivateUser(String username);
    boolean verifyLogin(String username, String password);


}