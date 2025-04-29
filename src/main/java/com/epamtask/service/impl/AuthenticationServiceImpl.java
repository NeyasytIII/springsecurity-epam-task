package com.epamtask.service.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.UserCredentials;
import com.epamtask.repository.UserCredentialsRepository;
import com.epamtask.service.AuthenticationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(UserCredentialsRepository userCredentialsRepository,
                                     PasswordEncoder passwordEncoder) {
        this.userCredentialsRepository = userCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Loggable
    @Override
    public boolean authenticate(String username, String rawPassword) {
        return userCredentialsRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(rawPassword, user.getHashedPassword()))
                .orElse(false);
    }

    @Loggable
    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        UserCredentials credentials = userCredentialsRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(oldPassword, credentials.getHashedPassword())) {
            throw new IllegalArgumentException("Invalid old password");
        }

        credentials.setHashedPassword(passwordEncoder.encode(newPassword));
        userCredentialsRepository.save(credentials);
    }

    @Loggable
    @Override
    public void register(String username, String rawPassword, String role) {
        if (userCredentialsRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        UserCredentials credentials = new UserCredentials();
        credentials.setUsername(username);
        credentials.setHashedPassword(passwordEncoder.encode(rawPassword));
        credentials.setRole(role);

        userCredentialsRepository.save(credentials);
    }
}