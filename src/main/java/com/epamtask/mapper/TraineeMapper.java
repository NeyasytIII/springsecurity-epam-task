package com.epamtask.mapper;

import com.epamtask.dto.traineedto.*;
import com.epamtask.model.Trainee;

import java.util.List;
import java.util.Set;

public interface TraineeMapper {
    Trainee toEntity(TraineeRegistrationRequestDto dto);
    Trainee toEntity(TraineeUpdateRequestDto dto, Trainee existing);
    TraineeProfileResponseDto toProfileDto(Trainee trainee);
    TraineeShortDto toShortDto(Trainee trainee);
    List<TraineeShortDto> toShortDtoList(Set<Trainee> trainees);
}