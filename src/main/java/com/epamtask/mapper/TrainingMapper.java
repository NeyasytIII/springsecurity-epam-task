package com.epamtask.mapper;

import com.epamtask.dto.trainingdto.*;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;

import java.util.List;

public interface TrainingMapper {
    Training toEntity(TrainingCreateRequestDto dto, Trainee trainee, Trainer trainer);
    TrainingResponseDto toDto(Training training);
    List<TrainingResponseDto> toDtoList(List<Training> trainings);
}