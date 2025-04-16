package com.epamtask.mapper.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.dto.trainingdto.TrainingCreateRequestDto;
import com.epamtask.dto.trainingdto.TrainingResponseDto;
import com.epamtask.mapper.TrainingMapper;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrainingMapperImpl implements TrainingMapper {

    @Loggable
    @Override
    public Training toEntity(TrainingCreateRequestDto dto, Trainee trainee, Trainer trainer) {
        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(dto.getTrainingName());
        training.setTrainingDate(convertToDate(dto.getTrainingDate()));
        training.setTrainingDuration(dto.getTrainingDuration());
        training.setType(dto.getTrainingType());
        return training;
    }

    @Loggable
    @Override
    public TrainingResponseDto toDto(Training training) {
        TrainingResponseDto dto = new TrainingResponseDto();
        dto.setTrainingName(training.getTrainingName());
        dto.setTrainingDate(training.getTrainingDate());
        dto.setTrainingType(training.getTrainer().getSpecialization());
        dto.setTrainingDuration(training.getTrainingDuration());
        dto.setTrainerName(training.getTrainer().getFirstName() + " " + training.getTrainer().getLastName());
        dto.setTraineeName(training.getTrainee().getFirstName() + " " + training.getTrainee().getLastName());
        return dto;
    }

    @Loggable
    @Override
    public List<TrainingResponseDto> toDtoList(List<Training> trainings) {
        return trainings.stream().map(this::toDto).collect(Collectors.toList());
    }

    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}