package com.epamtask.service.impl;

import com.epamtask.model.UserCredentials;
import com.epamtask.repository.UserCredentialsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest {

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_shouldReturnTrue_whenCredentialsValid() {
        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("Test.User");
        credentials.setHashedPassword("hashed");

        when(userCredentialsRepository.findByUsername("Test.User")).thenReturn(Optional.of(credentials));
        when(passwordEncoder.matches("rawPassword", "hashed")).thenReturn(true);

        assertTrue(authenticationService.authenticate("Test.User", "rawPassword"));
    }

    @Test
    void authenticate_shouldReturnFalse_whenUserNotFound() {
        when(userCredentialsRepository.findByUsername("NotExist.User")).thenReturn(Optional.empty());
        assertFalse(authenticationService.authenticate("NotExist.User", "anyPassword"));
    }

    @Test
    void authenticate_shouldReturnFalse_whenPasswordInvalid() {
        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("Test.User");
        credentials.setHashedPassword("hashed");

        when(userCredentialsRepository.findByUsername("Test.User")).thenReturn(Optional.of(credentials));
        when(passwordEncoder.matches("wrongPassword", "hashed")).thenReturn(false);

        assertFalse(authenticationService.authenticate("Test.User", "wrongPassword"));
    }

    @Test
    void changePassword_shouldSucceed_whenOldPasswordCorrect() {
        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("Test.User");
        credentials.setHashedPassword("oldHashed");

        when(userCredentialsRepository.findByUsername("Test.User")).thenReturn(Optional.of(credentials));
        when(passwordEncoder.matches("oldPassword", "oldHashed")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newHashed");

        authenticationService.changePassword("Test.User", "oldPassword", "newPassword");

        verify(userCredentialsRepository).save(credentials);
        assertEquals("newHashed", credentials.getHashedPassword());
    }

    @Test
    void changePassword_shouldThrow_whenOldPasswordInvalid() {
        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("Test.User");
        credentials.setHashedPassword("oldHashed");

        when(userCredentialsRepository.findByUsername("Test.User")).thenReturn(Optional.of(credentials));
        when(passwordEncoder.matches("wrongOldPassword", "oldHashed")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                authenticationService.changePassword("Test.User", "wrongOldPassword", "newPassword"));
    }

    @Test
    void register_shouldSaveNewUser() {
        when(userCredentialsRepository.findByUsername("New.User")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        authenticationService.register("New.User", "password123", "TRAINEE");

        verify(userCredentialsRepository).save(any(UserCredentials.class));
    }

    @Test
    void register_shouldThrow_whenUsernameAlreadyExists() {
        when(userCredentialsRepository.findByUsername("Existing.User")).thenReturn(Optional.of(new UserCredentials()));

        assertThrows(IllegalArgumentException.class, () ->
                authenticationService.register("Existing.User", "password", "TRAINER"));
    }
}