package com.epamtask.facade.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.dto.authenticationdto.AuthResponseDto;
import com.epamtask.dto.authenticationdto.AuthTokenResponseDto;
import com.epamtask.dto.authenticationdto.LoginRequestDto;
import com.epamtask.dto.authenticationdto.PasswordChangeRequestDto;
import com.epamtask.dto.traineedto.TraineeRegistrationRequestDto;
import com.epamtask.dto.trainerdto.TrainerRegistrationRequestDto;
import com.epamtask.exception.InvalidCredentialsException;
import com.epamtask.facade.AuthenticationFacade;
import com.epamtask.facade.TraineeFacade;
import com.epamtask.facade.TrainerFacade;
import com.epamtask.mapper.AuthMapper;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.security.AuthSessionStore;
import com.epamtask.service.AuthenticationService;
import com.epamtask.utils.PasswordGenerator;
import com.epamtask.utils.UserNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private final TraineeFacade traineeFacade;
    private final TrainerFacade trainerFacade;
    private final AuthenticationService authenticationService;
    private final AuthMapper authMapper;
    private final UserNameGenerator userNameGenerator;
    private final AuthSessionStore sessionStore;

    @Autowired
    public AuthenticationFacadeImpl(
            TraineeFacade traineeFacade,
            TrainerFacade trainerFacade,
            AuthenticationService authenticationService,
            AuthMapper authMapper,
            UserNameGenerator userNameGenerator,
            AuthSessionStore sessionStore
    ) {
        this.traineeFacade = traineeFacade;
        this.trainerFacade = trainerFacade;
        this.authenticationService = authenticationService;
        this.authMapper = authMapper;
        this.userNameGenerator = userNameGenerator;
        this.sessionStore = sessionStore;
    }

    @Loggable
    @Override
    public AuthResponseDto registerTrainee(TraineeRegistrationRequestDto dto) {
        String username = userNameGenerator.generateUserName(dto.getFirstName(), dto.getLastName());
        String password = PasswordGenerator.generatePassword();

        traineeFacade.createTrainee(dto.getFirstName(), dto.getLastName(), dto.getAddress(), dto.getBirthdayDate());
        traineeFacade.setInitialPassword(username, password);

        Trainee trainee = new Trainee();
        trainee.setUserName(username);
        trainee.setPassword(password);

        return authMapper.toAuthResponse(trainee);
    }

    @Loggable
    @Override
    public AuthResponseDto registerTrainer(TrainerRegistrationRequestDto dto) {
        String username = userNameGenerator.generateUserName(dto.getFirstName(), dto.getLastName());
        String password = PasswordGenerator.generatePassword();

        trainerFacade.createTrainer(dto.getFirstName(), dto.getLastName(), dto.getSpecialization());
        trainerFacade.setInitialPassword(username, password);

        Trainer trainer = new Trainer();
        trainer.setUserName(username);
        trainer.setPassword(password);
        return authMapper.toAuthResponse(trainer);
    }

    @Loggable
    @Override
    public AuthTokenResponseDto login(LoginRequestDto dto) {
        if (!authenticationService.authenticate(dto.getUsername(), dto.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        String token = sessionStore.createToken(dto.getUsername(), dto.getPassword());
        return new AuthTokenResponseDto(token);
    }
    @Loggable
    @Override
    public void changePassword(PasswordChangeRequestDto dto) {
        if (!authenticationService.authenticate(dto.getUsername(), dto.getOldPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        authenticationService.updatePasswordWithoutAuth(dto.getUsername(), dto.getNewPassword());
    }
}
