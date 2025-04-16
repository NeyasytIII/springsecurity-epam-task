package com.epamtask.mapper.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.dto.traineedto.TraineeProfileResponseDto;
import com.epamtask.dto.traineedto.TraineeRegistrationRequestDto;
import com.epamtask.dto.traineedto.TraineeShortDto;
import com.epamtask.dto.traineedto.TraineeUpdateRequestDto;
import com.epamtask.dto.trainerdto.TrainerShortDto;
import com.epamtask.mapper.TraineeMapper;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TraineeMapperImpl implements TraineeMapper {
    @Loggable
    @Override
    public Trainee toEntity(TraineeRegistrationRequestDto dto) {
        Trainee trainee = new Trainee();
        trainee.setFirstName(dto.getFirstName());
        trainee.setLastName(dto.getLastName());
        trainee.setAddress(dto.getAddress());
        trainee.setBirthdayDate(dto.getBirthdayDate());
        return trainee;
    }

    @Loggable
    @Override
    public Trainee toEntity(TraineeUpdateRequestDto dto, Trainee existing) {
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setAddress(dto.getAddress());
        existing.setBirthdayDate(dto.getBirthdayDate());
        existing.setActive(dto.getIsActive());
        return existing;
    }

    @Loggable
    @Override
    public TraineeProfileResponseDto toProfileDto(Trainee trainee) {
        TraineeProfileResponseDto dto = new TraineeProfileResponseDto();
        dto.setFirstName(trainee.getFirstName());
        dto.setLastName(trainee.getLastName());
        dto.setAddress(trainee.getAddress());
        dto.setBirthdayDate(trainee.getBirthdayDate());
        dto.setActive(trainee.isActive());
        dto.setTrainers(toTrainerShortDtoList(trainee.getTrainers()));
        return dto;
    }

    @Loggable
    @Override
    public TraineeShortDto toShortDto(Trainee trainee) {
        TraineeShortDto dto = new TraineeShortDto();
        dto.setUsername(trainee.getUserName());
        dto.setFirstName(trainee.getFirstName());
        dto.setLastName(trainee.getLastName());
        return dto;
    }

    @Loggable
    @Override
    public List<TraineeShortDto> toShortDtoList(Set<Trainee> trainees) {
        return trainees.stream().map(this::toShortDto).collect(Collectors.toList());
    }

    @Loggable
    private List<TrainerShortDto> toTrainerShortDtoList(Set<Trainer> trainers) {
        return trainers.stream().map(trainer -> {
            TrainerShortDto dto = new TrainerShortDto();
            dto.setUsername(trainer.getUserName());
            dto.setFirstName(trainer.getFirstName());
            dto.setLastName(trainer.getLastName());
            if (trainer.getSpecializationType() != null) {
                dto.setSpecialization(trainer.getSpecializationType().getType().name());
            } else {
                dto.setSpecialization(null);
            }
            return dto;
        }).collect(Collectors.toList());
    }
}
