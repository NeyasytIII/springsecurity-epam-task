package com.epamtask.mapper.impl;

import com.epamtask.dto.errordto.ErrorResponseDto;
import com.epamtask.dto.errordto.ValidationErrorDto;
import com.epamtask.mapper.ErrorMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ErrorMapperImplTest {

    private ErrorMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ErrorMapperImpl();
    }

    @Test
    void toValidationDto_shouldMapErrorsCorrectly() {
        FieldError error1 = new FieldError("object", "field1", "must not be null");
        FieldError error2 = new FieldError("object", "field2", "must be valid");

        List<FieldError> errors = List.of(error1, error2);

        ValidationErrorDto dto = mapper.toValidationDto(errors);

        assertEquals(2, dto.getErrors().size());
        assertTrue(dto.getErrors().get(0).contains("field1"));
        assertTrue(dto.getErrors().get(1).contains("field2"));
    }

    @Test
    void toErrorDto_shouldMapMessageCorrectly() {
        ErrorResponseDto dto = mapper.toErrorDto("Something went wrong");

        assertEquals("Something went wrong", dto.getMessage());
        assertEquals("RuntimeException", dto.getException());
        assertNotNull(dto.getTimestamp());
    }
}