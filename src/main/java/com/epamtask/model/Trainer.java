package com.epamtask.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Trainer extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("userId")
    private Long trainerId;

    @Column(nullable = false)
    private String specialization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_fk")
    private TrainingTypeEntity specializationType;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "trainer_trainee",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "trainee_id")
    )
    private Set<Trainee> trainees = new HashSet<>();

    public Trainer() {
    }

    public Trainer(Long trainerId, String firstName, String lastName, String specialization, boolean isActive) {
        super(firstName, lastName, isActive);
        this.trainerId = trainerId;
        this.specialization = specialization;
    }
    public Trainer(Long trainerId) {
        this.trainerId = trainerId;
    }
    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public TrainingTypeEntity getSpecializationType() {
        return specializationType;
    }

    public void setSpecializationType(TrainingTypeEntity specializationType) {
        this.specializationType = specializationType;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }

    public Set<Trainee> getTrainees() {
        return trainees;
    }

    public void setTrainees(Set<Trainee> trainees) {
        this.trainees = trainees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainer trainer = (Trainer) o;
        return Objects.equals(trainerId, trainer.trainerId)
                && Objects.equals(specialization, trainer.specialization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainerId, specialization);
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "userId=" + trainerId +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", userName='" + getUserName() + '\'' +
                ", specialization='" + specialization + '\'' +
                ", specializationType=" + specializationType +
                ", password='" + getPassword() + '\'' +
                ", isActive=" + isActive() +
                '}';
    }
}