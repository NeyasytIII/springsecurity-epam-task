package com.epamtask.mapper.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.dto.trainerdto.TrainerProfileResponseDto;
import com.epamtask.dto.trainerdto.TrainerRegistrationRequestDto;
import com.epamtask.dto.trainerdto.TrainerShortDto;
import com.epamtask.dto.trainerdto.TrainerUpdateRequestDto;
import com.epamtask.dto.traineedto.TraineeShortDto;
import com.epamtask.mapper.TrainerMapper;
import com.epamtask.model.Trainer;
import com.epamtask.model.Trainee;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TrainerMapperImpl implements TrainerMapper {
    @Loggable
    @Override
    public Trainer toEntity(TrainerRegistrationRequestDto dto) {
        Trainer trainer = new Trainer();
        trainer.setFirstName(dto.getFirstName());
        trainer.setLastName(dto.getLastName());
        trainer.setSpecialization(dto.getSpecialization());
        return trainer;
    }

    @Loggable
    @Override
    public Trainer toEntity(TrainerUpdateRequestDto dto, Trainer existing) {
        if (dto.getFirstName() != null) {
            existing.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            existing.setLastName(dto.getLastName());
        }
        if (dto.getIsActive() != null) {
            existing.setActive(dto.getIsActive());
        }
        return existing;
    }

    @Loggable
    @Override
    public TrainerProfileResponseDto toProfileDto(Trainer trainer) {
        TrainerProfileResponseDto dto = new TrainerProfileResponseDto();
        dto.setFirstName(trainer.getFirstName());
        dto.setLastName(trainer.getLastName());
        dto.setSpecialization(trainer.getSpecialization());
        dto.setActive(trainer.isActive());
        dto.setTrainees(toTraineeShortDtoList(trainer.getTrainees()));
        return dto;
    }

    @Loggable
    @Override
    public TrainerShortDto toShortDto(Trainer trainer) {
        TrainerShortDto dto = new TrainerShortDto();
        dto.setUsername(trainer.getUserName());
        dto.setFirstName(trainer.getFirstName());
        dto.setLastName(trainer.getLastName());
        dto.setSpecialization(trainer.getSpecialization());
        return dto;
    }

    @Loggable
    @Override
    public List<TrainerShortDto> toShortDtoList(Set<Trainer> trainers) {
        return trainers.stream().map(this::toShortDto).collect(Collectors.toList());
    }

    @Loggable
    private List<TraineeShortDto> toTraineeShortDtoList(Set<Trainee> trainees) {
        return trainees.stream().map(trainee -> {
            TraineeShortDto dto = new TraineeShortDto();
            dto.setUsername(trainee.getUserName());
            dto.setFirstName(trainee.getFirstName());
            dto.setLastName(trainee.getLastName());
            return dto;
        }).collect(Collectors.toList());
    }
}