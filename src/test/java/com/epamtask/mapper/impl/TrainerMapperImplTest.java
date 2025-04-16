package com.epamtask.mapper.impl;

import com.epamtask.dto.trainerdto.TrainerProfileResponseDto;
import com.epamtask.dto.trainerdto.TrainerRegistrationRequestDto;
import com.epamtask.dto.trainerdto.TrainerShortDto;
import com.epamtask.dto.trainerdto.TrainerUpdateRequestDto;
import com.epamtask.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainerMapperImplTest {

    private TrainerMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new TrainerMapperImpl();
    }

    @Test
    void toEntity_fromRegistrationDto_shouldMapCorrectly() {
        TrainerRegistrationRequestDto dto = new TrainerRegistrationRequestDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setSpecialization("CARDIO");

        Trainer result = mapper.toEntity(dto);

        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("CARDIO", result.getSpecialization());
    }

    @Test
    void toEntity_fromUpdateDto_shouldUpdateFields() {
        Trainer existing = new Trainer();
        existing.setFirstName("Old");
        existing.setLastName("Name");
        existing.setActive(false);

        TrainerUpdateRequestDto dto = new TrainerUpdateRequestDto();
        dto.setFirstName("New");
        dto.setLastName("Name");
        dto.setIsActive(true);

        Trainer updated = mapper.toEntity(dto, existing);

        assertEquals("New", updated.getFirstName());
        assertEquals("Name", updated.getLastName());
        assertTrue(updated.isActive());
    }

    @Test
    void toProfileDto_shouldMapAllFields() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("Anna");
        trainer.setLastName("Smith");
        trainer.setSpecialization("STRENGTH");
        trainer.setActive(true);
        trainer.setTrainees(new HashSet<>());

        TrainerProfileResponseDto dto = mapper.toProfileDto(trainer);

        assertEquals("Anna", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("STRENGTH", dto.getSpecialization());
        assertTrue(dto.isActive());
        assertNotNull(dto.getTrainees());
    }

    @Test
    void toShortDto_shouldMapBasicFields() {
        Trainer trainer = new Trainer();
        trainer.setUserName("anna.smith");
        trainer.setFirstName("Anna");
        trainer.setLastName("Smith");
        trainer.setSpecialization("CARDIO");

        TrainerShortDto dto = mapper.toShortDto(trainer);

        assertEquals("anna.smith", dto.getUsername());
        assertEquals("Anna", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("CARDIO", dto.getSpecialization());
    }

    @Test
    void toShortDtoList_shouldMapListCorrectly() {
        Trainer trainer = new Trainer();
        trainer.setUserName("t1");
        trainer.setFirstName("A");
        trainer.setLastName("B");
        trainer.setSpecialization("CARDIO");

        Set<Trainer> trainers = Set.of(trainer);

        List<TrainerShortDto> list = mapper.toShortDtoList(trainers);

        assertEquals(1, list.size());
        assertEquals("t1", list.get(0).getUsername());
    }
}