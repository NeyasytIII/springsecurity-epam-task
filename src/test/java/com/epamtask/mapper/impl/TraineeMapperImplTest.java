package com.epamtask.mapper.impl;

import com.epamtask.dto.traineedto.TraineeProfileResponseDto;
import com.epamtask.dto.traineedto.TraineeRegistrationRequestDto;
import com.epamtask.dto.traineedto.TraineeShortDto;
import com.epamtask.model.Trainee;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TraineeMapperImplTest {

    private final TraineeMapperImpl mapper = new TraineeMapperImpl();

    @Test
    void toEntity_fromRegistrationDto() {
        TraineeRegistrationRequestDto dto = new TraineeRegistrationRequestDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setAddress("Main St");
        dto.setBirthdayDate(new Date());

        Trainee trainee = mapper.toEntity(dto);

        assertEquals("John", trainee.getFirstName());
        assertEquals("Doe", trainee.getLastName());
        assertEquals("Main St", trainee.getAddress());
        assertNotNull(trainee.getBirthdayDate());
    }

    @Test
    void toShortDtoList_shouldMapCorrectly() {
        Trainee trainee1 = new Trainee();
        trainee1.setUserName("j.doe");
        trainee1.setFirstName("John");
        trainee1.setLastName("Doe");

        List<TraineeShortDto> dtos = mapper.toShortDtoList(Set.of(trainee1));

        assertEquals(1, dtos.size());
        TraineeShortDto dto = dtos.get(0);
        assertEquals("j.doe", dto.getUsername());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
    }

    @Test
    void toProfileDto_shouldMapCorrectly() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setAddress("Main St");
        trainee.setBirthdayDate(new Date());
        trainee.setActive(true);
        trainee.setTrainers(Set.of());

        TraineeProfileResponseDto dto = mapper.toProfileDto(trainee);

        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("Main St", dto.getAddress());
        assertTrue(dto.isActive());
        assertNotNull(dto.getBirthdayDate());
        assertNotNull(dto.getTrainers());
    }
}
