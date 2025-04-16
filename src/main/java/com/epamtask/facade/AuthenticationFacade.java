package com.epamtask.facade;

import com.epamtask.dto.authenticationdto.AuthResponseDto;
import com.epamtask.dto.authenticationdto.LoginRequestDto;
import com.epamtask.dto.authenticationdto.PasswordChangeRequestDto;
import com.epamtask.dto.authenticationdto.AuthTokenResponseDto;
import com.epamtask.dto.traineedto.TraineeRegistrationRequestDto;
import com.epamtask.dto.trainerdto.TrainerRegistrationRequestDto;

public interface AuthenticationFacade {
    AuthResponseDto registerTrainee(TraineeRegistrationRequestDto dto);
    AuthResponseDto registerTrainer(TrainerRegistrationRequestDto dto);
    AuthTokenResponseDto login(LoginRequestDto dto);
    void changePassword(PasswordChangeRequestDto dto);
}