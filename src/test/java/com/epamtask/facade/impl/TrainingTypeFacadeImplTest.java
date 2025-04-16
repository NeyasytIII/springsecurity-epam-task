package com.epamtask.facade.impl;
import com.epamtask.dto.trainingdto.TrainingTypeResponseDto;
import com.epamtask.model.TrainingType;
import com.epamtask.model.TrainingTypeEntity;
import com.epamtask.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class TrainingTypeFacadeImplTest {

    @Mock
    TrainingTypeService trainingTypeService;

    @InjectMocks
    TrainingTypeFacadeImpl trainingTypeFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTrainingTypes() {
        List<TrainingTypeEntity> types = List.of(new TrainingTypeEntity(1L, TrainingType.CARDIO));
        when(trainingTypeService.getAllTrainingTypes()).thenReturn(types);

        List<TrainingTypeResponseDto> result = trainingTypeFacade.getAllTrainingTypes();

        assertEquals(1, result.size());
        assertEquals("CARDIO", result.get(0).getType());
        verify(trainingTypeService).getAllTrainingTypes();
    }

    @Test
    void testGetTrainingTypeById() {
        TrainingTypeEntity type = new TrainingTypeEntity(2L, TrainingType.STRENGTH);
        when(trainingTypeService.getTrainingTypeById(2L)).thenReturn(Optional.of(type));

        Optional<TrainingTypeEntity> result = trainingTypeFacade.getTrainingTypeById(2L);

        assertTrue(result.isPresent());
        assertEquals("STRENGTH", result.get().getType().name());
        verify(trainingTypeService).getTrainingTypeById(2L);
    }

    @Test
    void testGetTrainingTypeByName() {
        TrainingTypeEntity type = new TrainingTypeEntity(3L, TrainingType.YOGA);
        when(trainingTypeService.getTrainingTypeByName("YOGA")).thenReturn(Optional.of(type));

        Optional<TrainingTypeEntity> result = trainingTypeFacade.getTrainingTypeByName("YOGA");

        assertTrue(result.isPresent());
        assertEquals("YOGA", result.get().getType().name());
        verify(trainingTypeService).getTrainingTypeByName("YOGA");
    }
}