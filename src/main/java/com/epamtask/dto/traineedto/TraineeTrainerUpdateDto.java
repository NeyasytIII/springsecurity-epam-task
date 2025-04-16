package com.epamtask.dto.traineedto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class TraineeTrainerUpdateDto {

    @NotBlank
    @JsonProperty("traineeUsername")
    private String traineeUsername;

    @NotEmpty
    @JsonProperty("trainerUsernames")
    private List<String> trainerUsernames;

    public String getTraineeUsername() {
        return traineeUsername;
    }

    public void setTraineeUsername(String traineeUsername) {
        this.traineeUsername = traineeUsername;
    }

    public List<String> getTrainerUsernames() {
        return trainerUsernames;
    }

    public void setTrainerUsernames(List<String> trainerUsernames) {
        this.trainerUsernames = trainerUsernames;
    }
}