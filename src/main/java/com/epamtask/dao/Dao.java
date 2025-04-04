package com.epamtask.dao;

import com.epamtask.model.Trainee;

import java.util.Map;
import java.util.Optional;

public interface Dao<ID, T> {
    void create(ID id, T entity);

    Map<Long, T> getAll();
}