package com.epamtask.controller;

import com.epamtask.dto.authenticationdto.LoginRequestDto;
import com.epamtask.dto.trainerdto.TrainerUpdateRequestDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TrainerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    private final String username = "John.Doe";
    private final String password = "pass123";

    @BeforeEach
    void setUp() throws Exception {
        LoginRequestDto login = new LoginRequestDto();
        login.setUsername(username);
        login.setPassword(password);

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        token = json.get("token").asText();
    }

    @Test
    void getTrainerProfile_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/trainers/" + username + "/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.specialization").value("STRENGTH"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void updateProfile_shouldReturn200() throws Exception {
        TrainerUpdateRequestDto dto = new TrainerUpdateRequestDto();
        dto.setFirstName("NewName");
        dto.setLastName("Updated");
        dto.setIsActive(true);

        mockMvc.perform(put("/api/trainers/" + username)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("NewName"));
    }

    @Test
    void toggleActivation_shouldReturn200() throws Exception {
        mockMvc.perform(patch("/api/trainers/" + username + "/status")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void getTrainerTrainings_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/trainers/" + username + "/trainings")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getTrainerTrainingsByFilter_shouldReturn200() throws Exception {
        String periodFrom = "2024-01-01";
        String periodTo = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        mockMvc.perform(get("/api/trainers/" + username + "/trainings")
                        .header("Authorization", "Bearer " + token)
                        .param("periodFrom", periodFrom)
                        .param("periodTo", periodTo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}