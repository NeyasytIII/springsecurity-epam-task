package com.epamtask.dto.traineedto;

import com.epamtask.dto.trainerdto.TrainerShortDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class TraineeProfileResponseDto {
    private String firstName;
    private String lastName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdayDate;
    private String address;
    private boolean isActive;
    private List<TrainerShortDto> trainers;


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthdayDate() {
        return birthdayDate;
    }

    public String getAddress() {
        return address;
    }

    public boolean isActive() {
        return isActive;
    }

    public List<TrainerShortDto> getTrainers() {
        return trainers;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthdayDate(Date birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setTrainers(List<TrainerShortDto> trainers) {
        this.trainers = trainers;
    }
}
