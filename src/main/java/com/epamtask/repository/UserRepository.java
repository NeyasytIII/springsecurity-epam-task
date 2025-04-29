package com.epamtask.repository;

import com.epamtask.model.User;
import java.util.Optional;

public interface UserRepository<T extends User> extends BaseRepository<T, Long> {
    Optional<T> findByUsername(String username);
    void activateUser(String username);
    void deactivateUser(String username);
    void deleteByUsername(String username);
}