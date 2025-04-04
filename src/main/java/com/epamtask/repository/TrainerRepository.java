package com.epamtask.repository;
import com.epamtask.model.Trainer;
import java.util.List;

public interface TrainerRepository extends UserRepository<Trainer> {
    List<Trainer> findNotAssignedToTrainee(String traineeUsername);

}