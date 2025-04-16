package com.epamtask.mapper.impl;

import com.epamtask.dto.authenticationdto.AuthResponseDto;
import com.epamtask.mapper.AuthMapper;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthMapperImplTest {

    private final AuthMapper authMapper = new AuthMapperImpl();

    @Test
    void toAuthResponse_fromTrainee_shouldMapCorrectly() {
        Trainee trainee = new Trainee();
        trainee.setUserName("John.Doe");
        trainee.setPassword("pass123");

        AuthResponseDto dto = authMapper.toAuthResponse(trainee);

        assertEquals("John.Doe", dto.getUsername());
        assertEquals("pass123", dto.getPassword());
    }

    @Test
    void toAuthResponse_fromTrainer_shouldMapCorrectly() {
        Trainer trainer = new Trainer();
        trainer.setUserName("Jane.Smith");
        trainer.setPassword("secure456");

        AuthResponseDto dto = authMapper.toAuthResponse(trainer);

        assertEquals("Jane.Smith", dto.getUsername());
        assertEquals("secure456", dto.getPassword());
    }
}