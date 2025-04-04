package com.epamtask.dao;

import java.util.Optional;

public interface UserDao<T> extends Dao<Long, T> {
    void update(T entity);
    Optional<T> findById(Long id);
    Optional<T> findByUsername(String username);
    void deleteById(Long id);

    void deleteByUsername(String username);
    void updatePassword(String username, String newPassword);
    void activateUser(String username);
    void deactivateUser(String username);
}