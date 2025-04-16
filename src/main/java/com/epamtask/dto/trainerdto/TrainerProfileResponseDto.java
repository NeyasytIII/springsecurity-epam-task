package com.epamtask.dto.trainerdto;

import com.epamtask.dto.traineedto.TraineeShortDto;

import java.util.List;

public class TrainerProfileResponseDto {
    private String firstName;
    private String lastName;
    private String specialization;
    private boolean isActive;
    private List<TraineeShortDto> trainees;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public boolean isActive() {
        return isActive;
    }

    public List<TraineeShortDto> getTrainees() {
        return trainees;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setTrainees(List<TraineeShortDto> trainees) {
        this.trainees = trainees;
    }
}
