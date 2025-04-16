package com.epamtask.mapper;

import com.epamtask.dto.trainingdto.TrainingTypeResponseDto;
import com.epamtask.model.TrainingTypeEntity;

import java.util.List;

public interface TrainingTypeMapper {
    TrainingTypeResponseDto toDto(TrainingTypeEntity entity);
    List<TrainingTypeResponseDto> toDtoList(List<TrainingTypeEntity> entities);
}