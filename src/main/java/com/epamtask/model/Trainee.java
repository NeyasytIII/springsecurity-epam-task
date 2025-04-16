package com.epamtask.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Trainee extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("userId")
    private Long traineeId;

    @Column(nullable = false)
    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private Date birthdayDate;

    @ManyToMany(mappedBy = "trainees")
    private Set<Trainer> trainers = new HashSet<>();

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings = new ArrayList<>();

    public Trainee() {
    }

    public Trainee(
            Long traineeId,
            String firstName,
            String lastName,
            String address,
            Date birthdayDate,
            boolean isActive
    ) {
        super(firstName, lastName, isActive);
        this.traineeId = traineeId;
        this.address = address;
        this.birthdayDate = birthdayDate;
    }

    public Trainee(Long traineeId) {
        this.traineeId = traineeId;
    }

    public Long getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(Long traineeId) {
        this.traineeId = traineeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(Date birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public Set<Trainer> getTrainers() {
        return trainers;
    }

    public void setTrainers(Set<Trainer> trainers) {
        this.trainers = trainers;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainee trainee = (Trainee) o;
        return Objects.equals(traineeId, trainee.traineeId)
                && Objects.equals(address, trainee.address)
                && Objects.equals(birthdayDate, trainee.birthdayDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traineeId, address, birthdayDate);
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "userId=" + traineeId +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", userName='" + getUserName() + '\'' +
                ", address='" + address + '\'' +
                ", birthday='" + birthdayDate + '\'' +
                ", password='" + getPassword() + '\'' +
                ", isActive=" + isActive() +
                '}';
    }
}