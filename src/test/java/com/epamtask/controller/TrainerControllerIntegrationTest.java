package com.epamtask.controller;

import com.epamtask.dto.trainerdto.TrainerUpdateRequestDto;
import com.epamtask.dto.trainingdto.TrainingFilterRequestDto;
import com.epamtask.security.AuthSessionStore;
import com.epamtask.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TrainerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthSessionStore sessionStore;

    @Autowired
    private AuthenticationService authenticationService;

    private String token;

    private final String username = "Michael.Scott";
    private final String password = "trainerpass";

    @BeforeEach
    void setUp() {
        authenticationService.updatePasswordWithoutAuth(username, password);
        token = sessionStore.createToken(username, password);
    }

    @Test
    void getTrainerProfile_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/trainers/" + username + "/profile")
                        .header("X-Auth-Token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Michael"))
                .andExpect(jsonPath("$.lastName").value("Scott"))
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
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("NewName"));
    }

    @Test
    void toggleActivation_shouldReturn200() throws Exception {
        mockMvc.perform(patch("/api/trainers/" + username + "/status")
                        .header("X-Auth-Token", token))
                .andExpect(status().isOk());
    }

    @Test
    void getTrainerTrainings_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/trainers/" + username + "/trainings")
                        .header("X-Auth-Token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getTrainerTrainingsByFilter_shouldReturn200() throws Exception {
        String periodFrom = "2024-01-01";
        String periodTo = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        mockMvc.perform(get("/api/trainers/" + username + "/trainings")
                        .header("X-Auth-Token", token)
                        .param("periodFrom", periodFrom)
                        .param("periodTo", periodTo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}