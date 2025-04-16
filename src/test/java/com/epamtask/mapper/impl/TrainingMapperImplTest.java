package com.epamtask.mapper.impl;

import com.epamtask.dto.trainingdto.TrainingCreateRequestDto;
import com.epamtask.dto.trainingdto.TrainingResponseDto;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import com.epamtask.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TrainingMapperImplTest {

    private TrainingMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new TrainingMapperImpl();
    }

    @Test
    void toEntity_shouldMapCorrectly() {
        TrainingCreateRequestDto dto = new TrainingCreateRequestDto();
        dto.setTrainingName("Yoga");
        dto.setTrainingDate(LocalDate.of(2025, 4, 15));
        dto.setTrainingDuration("1h");
        dto.setTrainingType(TrainingType.CARDIO);

        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();

        Training training = mapper.toEntity(dto, trainee, trainer);

        assertEquals("Yoga", training.getTrainingName());
        assertEquals("1h", training.getTrainingDuration());
        assertEquals(TrainingType.CARDIO, training.getType());
        assertEquals(trainee, training.getTrainee());
        assertEquals(trainer, training.getTrainer());
        assertNotNull(training.getTrainingDate());
    }

    @Test
    void toDto_shouldMapCorrectly() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Smith");

        Trainer trainer = new Trainer();
        trainer.setFirstName("Anna");
        trainer.setLastName("Doe");
        trainer.setSpecialization("CARDIO");

        Training training = new Training();
        training.setTrainingName("Pilates");
        training.setTrainingDate(new Date());
        training.setTrainingDuration("2h");
        training.setTrainer(trainer);
        training.setTrainee(trainee);

        TrainingResponseDto dto = mapper.toDto(training);

        assertEquals("Pilates", dto.getTrainingName());
        assertEquals("Anna Doe", dto.getTrainerName());
        assertEquals("John Smith", dto.getTraineeName());
        assertEquals("CARDIO", dto.getTrainingType());
        assertEquals("2h", dto.getTrainingDuration());
        assertNotNull(dto.getTrainingDate());
    }

    @Test
    void toDtoList_shouldMapListCorrectly() {
        Training training = new Training();
        training.setTrainingName("Session");

        Trainer trainer = new Trainer();
        trainer.setFirstName("T");
        trainer.setLastName("L");
        trainer.setSpecialization("STRENGTH");

        Trainee trainee = new Trainee();
        trainee.setFirstName("A");
        trainee.setLastName("B");

        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingDuration("45m");
        training.setTrainingDate(new Date());

        List<TrainingResponseDto> result = mapper.toDtoList(List.of(training));

        assertEquals(1, result.size());
        assertEquals("Session", result.get(0).getTrainingName());
    }
}