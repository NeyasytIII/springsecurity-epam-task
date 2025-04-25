package com.epamtask.mapper.impl;

import com.epamtask.dto.errordto.ErrorResponseDto;
import com.epamtask.dto.errordto.ValidationErrorDto;
import com.epamtask.mapper.ErrorMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ErrorMapperImpl implements ErrorMapper {

    @Override
    public ValidationErrorDto toValidationDto(List<FieldError> errors) {
        List<String> errorMessages = errors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ValidationErrorDto dto = new ValidationErrorDto();
        dto.setErrors(errorMessages);
        return dto;
    }

    @Override
    public ErrorResponseDto toErrorDto(String message) {
        ErrorResponseDto dto = new ErrorResponseDto();
        dto.setMessage(message);
        dto.setException("RuntimeException");
        dto.setTimestamp(java.time.LocalDateTime.now());
        return dto;
    }
    public ErrorResponseDto toErrorResponse(String message) {
        ErrorResponseDto dto = new ErrorResponseDto();
        dto.setException("ResponseStatusException");
        dto.setMessage(message);
        dto.setTimestamp(LocalDateTime.now());
        dto.setDetails(List.of("Unexpected error occurred"));
        return dto;
    }
}