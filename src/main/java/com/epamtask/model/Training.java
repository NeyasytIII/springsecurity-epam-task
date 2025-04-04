package com.epamtask.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.Objects;

@Entity
@Table
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainingId;

    @ManyToOne
    @JoinColumn(name = "trainer_id", referencedColumnName = "trainerId", nullable = false)
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name = "trainee_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Trainee trainee;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "training_type_id", nullable = false)
    private TrainingTypeEntity trainingType;

    @Column(nullable = false)
    private String trainingName;

    @Column(nullable = false)
    private Date trainingDate;

    @Column(nullable = false)
    private String trainingDuration;

    public Training(Long trainingId, String trainingName, TrainingTypeEntity trainingType, Date trainingDate, String trainingDuration) {
        this.trainingId = trainingId;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public Training(Trainee trainee, Trainer trainer, TrainingTypeEntity trainingType, String trainingName, Date trainingDate, String trainingDuration) {
        this.trainee = trainee;
        this.trainer = trainer;
        this.trainingType = trainingType;
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public Training() {
    }

    public Long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    public TrainingTypeEntity getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingTypeEntity trainingType) {
        this.trainingType = trainingType;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public String getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(String trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    public TrainingType getType() {
        return (trainingType != null) ? trainingType.getType() : null;
    }

    public void setType(TrainingType enumType) {
        if (enumType == null) {
            this.trainingType = null;
        } else if (this.trainingType == null) {
            this.trainingType = new TrainingTypeEntity(enumType);
        } else {
            this.trainingType.setType(enumType);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return Objects.equals(trainingId, training.trainingId)
                && Objects.equals(trainingType, training.trainingType)
                && Objects.equals(trainingName, training.trainingName)
                && Objects.equals(trainingDate, training.trainingDate)
                && Objects.equals(trainingDuration, training.trainingDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                trainingId,
                trainingType,
                trainingName,
                trainingDate,
                trainingDuration
        );
    }

    @Override
    public String toString() {
        return "Training{" +
                "trainingId=" + trainingId +
                ", trainee=" + (trainee != null ? trainee.getTraineeId() : null) +
                ", trainer=" + (trainer != null ? trainer.getTrainerId() : null) +
                ", trainingType=" + trainingType +
                ", trainingName='" + trainingName + '\'' +
                ", trainingDate=" + trainingDate +
                ", trainingDuration='" + trainingDuration + '\'' +
                '}';
    }
}
