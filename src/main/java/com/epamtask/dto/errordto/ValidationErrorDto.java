package com.epamtask.dto.errordto;

import java.util.List;

public class ValidationErrorDto {
    private List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}