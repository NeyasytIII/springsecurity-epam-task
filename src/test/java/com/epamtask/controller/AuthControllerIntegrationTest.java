package com.epamtask.controller;

import com.epamtask.dto.authenticationdto.LoginRequestDto;
import com.epamtask.dto.authenticationdto.PasswordChangeRequestDto;
import com.epamtask.dto.traineedto.TraineeRegistrationRequestDto;
import com.epamtask.dto.trainerdto.TrainerRegistrationRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String username;
    private String password;
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        TraineeRegistrationRequestDto regDto = new TraineeRegistrationRequestDto();
        long rnd = ThreadLocalRandom.current().nextLong(1000000);
        regDto.setFirstName("John" + rnd);
        regDto.setLastName("Doe" + rnd);
        regDto.setAddress("Test Address");
        regDto.setBirthdayDate(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"));

        String response = mockMvc.perform(post("/api/auth/trainees/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        username = jsonNode.get("username").asText();
        password = jsonNode.get("password").asText();
        token = jsonNode.get("token").asText();
    }

    @Test
    void login_shouldReturnToken() throws Exception {
        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setUsername(username);
        loginDto.setPassword(password);

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(loginResponse);
        assertNotNull(jsonNode.get("token").asText());
    }

    @Test
    void changePassword_shouldSucceed() throws Exception {
        PasswordChangeRequestDto changeDto = new PasswordChangeRequestDto();
        changeDto.setUsername(username);
        changeDto.setOldPassword(password);
        changeDto.setNewPassword("NewPass!123");

        mockMvc.perform(put("/api/auth/change-password")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeDto)))
                .andExpect(status().isOk());

        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setUsername(username);
        loginDto.setPassword("NewPass!123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk());
    }

    @Test
    void logout_shouldSucceed() throws Exception {
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void registerTrainer_shouldReturn200() throws Exception {
        TrainerRegistrationRequestDto trainerDto = new TrainerRegistrationRequestDto();
        long rnd = ThreadLocalRandom.current().nextLong(1000000);
        trainerDto.setFirstName("Trainer" + rnd);
        trainerDto.setLastName("Test" + rnd);
        trainerDto.setSpecialization("STRENGTH");

        mockMvc.perform(post("/api/auth/trainers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerDto)))
                .andExpect(status().isOk());
    }
}