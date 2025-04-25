package com.epamtask.controller;

import com.epamtask.dto.traineedto.TraineeRegistrationRequestDto;
import com.epamtask.dto.trainerdto.TrainerRegistrationRequestDto;
import com.epamtask.dto.trainingdto.TrainingCreateRequestDto;
import com.epamtask.model.TrainingType;
import com.epamtask.security.AuthSessionStore;
import com.epamtask.service.AuthenticationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TrainingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthSessionStore sessionStore;

    @Autowired
    private AuthenticationService authenticationService;

    private String token;
    private String traineeUsername;
    private String trainerUsername;

    @BeforeEach
    void setUp() throws Exception {
        long timestamp = System.currentTimeMillis();

        TraineeRegistrationRequestDto traineeDto = new TraineeRegistrationRequestDto();
        traineeDto.setFirstName("Cascade" + timestamp);
        traineeDto.setLastName("Trainee");
        traineeDto.setAddress("Cascade St");
        traineeDto.setBirthdayDate(Date.valueOf("1999-12-31"));

        String traineeResponse = mockMvc.perform(post("/api/auth/trainees/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traineeDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode traineeJson = objectMapper.readTree(traineeResponse);
        traineeUsername = traineeJson.get("username").asText();
        String traineePassword = traineeJson.get("password").asText();

        authenticationService.updatePasswordWithoutAuth(traineeUsername, traineePassword);
        assertTrue(authenticationService.authenticate(traineeUsername, traineePassword));
        token = sessionStore.createToken(traineeUsername, traineePassword);

        TrainerRegistrationRequestDto trainerDto = new TrainerRegistrationRequestDto();
        trainerDto.setFirstName("Michael" + timestamp);
        trainerDto.setLastName("Scott");
        trainerDto.setSpecialization("STRENGTH");

        String trainerResponse = mockMvc.perform(post("/api/auth/trainers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode trainerJson = objectMapper.readTree(trainerResponse);
        trainerUsername = trainerJson.get("username").asText();
        String trainerPassword = trainerJson.get("password").asText();

        authenticationService.updatePasswordWithoutAuth(trainerUsername, trainerPassword);
        assertTrue(authenticationService.authenticate(trainerUsername, trainerPassword));
    }

    @Test
    void addTraining_shouldReturn200() throws Exception {
        TrainingCreateRequestDto dto = new TrainingCreateRequestDto();
        dto.setTraineeUsername(traineeUsername);
        dto.setTrainerUsername(trainerUsername);
        dto.setTrainingName("Spring Basics");
        dto.setTrainingDate(LocalDate.now());
        dto.setTrainingDuration("60");
        dto.setTrainingType(TrainingType.STRENGTH);

        mockMvc.perform(post("/api/training")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}