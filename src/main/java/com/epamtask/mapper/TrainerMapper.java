package com.epamtask.mapper;

import com.epamtask.dto.trainerdto.*;
import com.epamtask.model.Trainer;

import java.util.List;
import java.util.Set;

public interface TrainerMapper {
    Trainer toEntity(TrainerRegistrationRequestDto dto);
    Trainer toEntity(TrainerUpdateRequestDto dto, Trainer existing);
    TrainerProfileResponseDto toProfileDto(Trainer trainer);
    TrainerShortDto toShortDto(Trainer trainer);
    List<TrainerShortDto> toShortDtoList(Set<Trainer> trainers);
}