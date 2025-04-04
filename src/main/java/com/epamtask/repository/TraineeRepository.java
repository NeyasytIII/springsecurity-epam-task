package com.epamtask.repository;

import com.epamtask.model.Trainee;
import jakarta.transaction.Transactional;

import java.util.List;

public interface TraineeRepository extends UserRepository<Trainee> {

    void updateTraineeTrainersList(String traineeUsername, List<String> trainerUsernames);
}