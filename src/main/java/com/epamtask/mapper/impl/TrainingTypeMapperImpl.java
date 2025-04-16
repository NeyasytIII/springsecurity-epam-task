package com.epamtask.mapper.impl;

import com.epamtask.dto.trainingdto.TrainingTypeResponseDto;
import com.epamtask.mapper.TrainingTypeMapper;
import com.epamtask.model.TrainingTypeEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrainingTypeMapperImpl implements TrainingTypeMapper {

    @Override
    public TrainingTypeResponseDto toDto(TrainingTypeEntity entity) {
        TrainingTypeResponseDto dto = new TrainingTypeResponseDto();
        dto.setId(entity.getId());
        dto.setType(entity.getType().name());
        return dto;
    }

    @Override
    public List<TrainingTypeResponseDto> toDtoList(List<TrainingTypeEntity> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }
}