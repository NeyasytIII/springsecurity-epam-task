package com.epamtask.controller;

import com.epamtask.dto.traineedto.TraineeRegistrationRequestDto;
import com.epamtask.dto.traineedto.TraineeTrainerUpdateDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TraineeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String traineeUsername;
    private String trainerUsername;
    private String traineeFirstName;
    private String traineeToken;

    @BeforeEach
    void setup() throws Exception {
        TraineeRegistrationRequestDto regDto = new TraineeRegistrationRequestDto();
        String unique = String.valueOf(System.currentTimeMillis());
        traineeFirstName = "Test" + unique;
        regDto.setFirstName(traineeFirstName);
        regDto.setLastName("User");
        regDto.setAddress("123 Street");
        regDto.setBirthdayDate(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"));

        String response = mockMvc.perform(post("/api/auth/trainees/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        traineeUsername = json.get("username").asText();
        String traineePassword = json.get("password").asText();

        String loginPayload = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", traineeUsername, traineePassword);
        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        traineeToken = objectMapper.readTree(loginResponse).get("token").asText();

        String trainerJson = """
        {
          "firstName": "Jane",
          "lastName": "Trainer",
          "specialization": "CARDIO"
        }
        """;

        String trainerResponse = mockMvc.perform(post("/api/auth/trainers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainerJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        trainerUsername = objectMapper.readTree(trainerResponse).get("username").asText();
    }

    @Test
    void getTraineeProfileSuccess() throws Exception {
        mockMvc.perform(get("/api/trainees/" + traineeUsername + "/profile")
                        .header("X-Auth-Token", traineeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(traineeFirstName));
    }




    @Test
    void deleteTraineeSuccess() throws Exception {
        mockMvc.perform(delete("/api/trainees/" + traineeUsername)
                        .header("X-Auth-Token", traineeToken))
                .andExpect(status().isOk());
    }


    @Test
    void toggleActivationSuccess() throws Exception {
        mockMvc.perform(patch("/api/trainees/" + traineeUsername + "/status")
                        .param("isActive", "false")
                        .header("X-Auth-Token", traineeToken))
                .andExpect(status().isOk());
    }

    @Test
    void updateTrainersSuccess() throws Exception {
        TraineeTrainerUpdateDto dto = new TraineeTrainerUpdateDto();
        dto.setTraineeUsername(traineeUsername);
        dto.setTrainerUsernames(List.of(trainerUsername));

        mockMvc.perform(put("/api/trainees/trainers")
                        .header("X-Auth-Token", traineeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value(trainerUsername));
    }

    @Test
    void getNotAssignedTrainersSuccess() throws Exception {
        mockMvc.perform(get("/api/trainees/" + traineeUsername + "/free-trainers")
                        .header("X-Auth-Token", traineeToken))
                .andExpect(status().isOk());
    }

    @Test
    void getTraineeTrainingsSuccess() throws Exception {
        mockMvc.perform(get("/api/trainees/" + traineeUsername + "/trainings")
                        .header("X-Auth-Token", traineeToken))
                .andExpect(status().isOk());
    }
}