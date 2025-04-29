package com.epamtask.service;

import com.epamtask.model.Trainee;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TraineeService {
  void createTrainee(String firstName, String lastName, String address, Date birthdayDate);
  void updateTrainee(Trainee trainee);
  void deleteTrainee(Long id);
  void deleteTraineeByUsername(String username);
  Optional<Trainee> getTraineeById(Long id);
  Optional<Trainee> getTraineeByUsername(String username);
  List<Trainee> getAllTrainees();
  void activateUser(String username);
  void deactivateUser(String username);
  void assignTrainersToTrainee(String traineeUsername, List<String> trainerUsernames);
}