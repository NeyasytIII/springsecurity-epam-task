package com.epamtask.facade;

import com.epamtask.model.Trainee;

import java.util.Date;
import java.util.List;
import java.util.Optional;
public interface TraineeFacade {
    void createTrainee(String firstName, String lastName, String address, Date birthdayDate);
    void updateTrainee(Trainee trainee);
    void deleteTrainee(String username);
    Optional<Trainee> getTraineeById(Long id);
    Optional<Trainee> getTraineeByUsername(String username);
    List<Trainee> getAllTrainees();

    void updatePassword(String username, String newPassword);
    void activateTrainee(String username);
    void deactivateTrainee(String username);
    void assignTrainersToTrainee(String traineeUsername, List<String> trainerUsernames);
    boolean verifyLogin(String username, String password);
    void setInitialPassword(String username, String newPassword);
}