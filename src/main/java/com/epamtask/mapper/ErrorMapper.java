package com.epamtask.mapper;

import com.epamtask.dto.errordto.ErrorResponseDto;
import com.epamtask.dto.errordto.ValidationErrorDto;
import org.springframework.validation.FieldError;

import java.util.List;

public interface ErrorMapper {
    ValidationErrorDto toValidationDto(List<FieldError> errors);
    ErrorResponseDto toErrorDto(String message);
}