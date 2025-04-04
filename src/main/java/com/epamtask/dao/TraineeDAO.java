package com.epamtask.dao;

import com.epamtask.model.Trainee;

public interface TraineeDAO extends UserDao<Trainee> {
    boolean verifyLogin(String username, String password);
    boolean existsByUsernameAndPassword(String username, String password);
}