package com.epamtask.facade;

import com.epamtask.dto.authenticationdto.AuthTokenResponseDto;
import com.epamtask.dto.authenticationdto.LoginRequestDto;
import com.epamtask.dto.authenticationdto.PasswordChangeRequestDto;
import com.epamtask.dto.authenticationdto.RegisterResponseDto;
import com.epamtask.dto.traineedto.TraineeRegistrationRequestDto;
import com.epamtask.dto.trainerdto.TrainerRegistrationRequestDto;

public interface AuthenticationFacade {
    RegisterResponseDto registerTrainee(TraineeRegistrationRequestDto dto);
    RegisterResponseDto registerTrainer(TrainerRegistrationRequestDto dto);
    AuthTokenResponseDto login(LoginRequestDto dto);
    void changePassword(String username, String oldPassword, String newPassword);
    void logout(String token);
}