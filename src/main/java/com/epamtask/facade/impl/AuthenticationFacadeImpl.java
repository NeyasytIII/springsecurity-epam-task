package com.epamtask.facade.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.dto.authenticationdto.AuthTokenResponseDto;
import com.epamtask.dto.authenticationdto.LoginRequestDto;
import com.epamtask.dto.authenticationdto.RegisterResponseDto;
import com.epamtask.dto.traineedto.TraineeRegistrationRequestDto;
import com.epamtask.dto.trainerdto.TrainerRegistrationRequestDto;
import com.epamtask.exception.InvalidCredentialsException;
import com.epamtask.facade.AuthenticationFacade;
import com.epamtask.facade.TraineeFacade;
import com.epamtask.facade.TrainerFacade;

import com.epamtask.security.JwtTokenProvider;
import com.epamtask.service.AuthenticationService;
import com.epamtask.service.LogoutService;
import com.epamtask.service.impl.BruteForceProtectionService;
import com.epamtask.utils.PasswordGenerator;
import com.epamtask.utils.UserNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private final TraineeFacade traineeFacade;
    private final TrainerFacade trainerFacade;
    private final AuthenticationService authenticationService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserNameGenerator userNameGenerator;
    private final BruteForceProtectionService bruteForceProtectionService;
    private final LogoutService logoutService;

    @Autowired
    public AuthenticationFacadeImpl(
            TraineeFacade traineeFacade,
            TrainerFacade trainerFacade,
            AuthenticationService authenticationService,
            JwtTokenProvider jwtTokenProvider,
            UserNameGenerator userNameGenerator,
            BruteForceProtectionService bruteForceProtectionService,
            LogoutService logoutService
    ) {
        this.traineeFacade = traineeFacade;
        this.trainerFacade = trainerFacade;
        this.authenticationService = authenticationService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userNameGenerator = userNameGenerator;
        this.bruteForceProtectionService = bruteForceProtectionService;
        this.logoutService =  logoutService;
    }

    @Loggable
    @Override
    public RegisterResponseDto registerTrainee(TraineeRegistrationRequestDto dto) {
        String generatedUsername = userNameGenerator.generateUserName(dto.getFirstName(), dto.getLastName());
        String generatedPassword = PasswordGenerator.generatePassword();
        traineeFacade.createTrainee(dto.getFirstName(), dto.getLastName(), dto.getAddress(), dto.getBirthdayDate());
        authenticationService.register(generatedUsername, generatedPassword, "TRAINEE");
        String token = jwtTokenProvider.generateToken(generatedUsername);
        return new RegisterResponseDto(token, generatedUsername, generatedPassword);
    }

    @Loggable
    @Override
    public RegisterResponseDto registerTrainer(TrainerRegistrationRequestDto dto) {
        String generatedUsername = userNameGenerator.generateUserName(dto.getFirstName(), dto.getLastName());
        String generatedPassword = PasswordGenerator.generatePassword();
        trainerFacade.createTrainer(dto.getFirstName(), dto.getLastName(), dto.getSpecialization());
        authenticationService.register(generatedUsername, generatedPassword, "TRAINER");
        String token = jwtTokenProvider.generateToken(generatedUsername);
        return new RegisterResponseDto(token, generatedUsername, generatedPassword);
    }

    @Loggable
    @Override
    public AuthTokenResponseDto login(LoginRequestDto dto) {
        if (bruteForceProtectionService.isLocked(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account temporarily locked due to too many failed attempts.");
        }

        if (!authenticationService.authenticate(dto.getUsername(), dto.getPassword())) {
            bruteForceProtectionService.recordFailedAttempt(dto.getUsername());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        bruteForceProtectionService.resetAttempts(dto.getUsername());
        String token = jwtTokenProvider.generateToken(dto.getUsername());
        return new AuthTokenResponseDto(token);
    }

    @Loggable
    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        authenticationService.changePassword(username, oldPassword, newPassword);
    }
    @Loggable
    @Override
    public void logout(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing or invalid Authorization header");
        }
        String token = authorizationHeader.substring(7);
        logoutService.logout(token);
    }

}