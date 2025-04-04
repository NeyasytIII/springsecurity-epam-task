package com.epamtask.service;

public interface AuthenticationService {
    boolean authenticate(String username, String password);
    boolean checkCredentialsWithoutAuth(String username, String password);
    void updatePasswordWithoutAuth(String username, String newPassword);
}
