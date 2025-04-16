package com.epamtask.dto.trainingdto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

public class TrainingFilterRequestDto {
    private String username;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date periodFrom;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date periodTo;
    private String trainerName;
    private String trainingType;

    public String getUsername() {
        return username;
    }

    public Date getPeriodFrom() {
        return periodFrom;
    }

    public Date getPeriodTo() {
        return periodTo;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPeriodFrom(Date periodFrom) {
        this.periodFrom = periodFrom;
    }

    public void setPeriodTo(Date periodTo) {
        this.periodTo = periodTo;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }
}
