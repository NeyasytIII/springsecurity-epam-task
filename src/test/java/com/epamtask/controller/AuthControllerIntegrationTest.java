package com.epamtask.controller;

import com.epamtask.dto.authenticationdto.*;
import com.epamtask.dto.traineedto.TraineeRegistrationRequestDto;
import com.epamtask.dto.trainerdto.TrainerRegistrationRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private static String traineeUsername;
    private static String traineePassword;
    private static boolean traineeInited;

    @BeforeAll
    static void disableContextCache() {
        System.setProperty("spring.test.context.cache.maxSize", "0");
    }

    @Test
    void trainee_registerLoginChangePassword_flowOk() throws Exception {
        ensureTrainee();

        LoginRequestDto login = new LoginRequestDto();
        login.setUsername(traineeUsername);
        login.setPassword(traineePassword);

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(login)))
                .andExpect(status().isOk());
        PasswordChangeRequestDto change = new PasswordChangeRequestDto();
        change.setUsername(traineeUsername);
        change.setOldPassword(traineePassword);
        change.setNewPassword("NewPass!123");

        mvc.perform(put("/api/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(change)))
                .andExpect(status().isOk());
        login.setPassword("NewPass!123");
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(login)))
                .andExpect(status().isOk());
    }

    @Test
    void trainee_loginWithWrongPassword_returns401() throws Exception {
        ensureTrainee();

        LoginRequestDto dto = new LoginRequestDto();
        dto.setUsername(traineeUsername);
        dto.setPassword("totally-wrong");

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void trainee_changePasswordWithWrongOldPassword_returns401() throws Exception {
        ensureTrainee();

        PasswordChangeRequestDto dto = new PasswordChangeRequestDto();
        dto.setUsername(traineeUsername);
        dto.setOldPassword("invalid-old");
        dto.setNewPassword("whatever");

        mvc.perform(put("/api/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    private void ensureTrainee() throws Exception {
        if (traineeInited) return;

        long rnd = ThreadLocalRandom.current().nextLong(1_000_000);
        TraineeRegistrationRequestDto reg = new TraineeRegistrationRequestDto();
        reg.setFirstName("John" + rnd);
        reg.setLastName("Doe" + rnd);
        reg.setAddress("улица Пушкина");
        reg.setBirthdayDate(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"));

        String resp = mvc.perform(post("/api/auth/trainees/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reg)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode j = mapper.readTree(resp);
        traineeUsername = j.get("username").asText();
        traineePassword = j.get("password").asText();
        traineeInited = true;
    }
}