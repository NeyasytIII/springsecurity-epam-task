package com.epamtask.dto.errordto;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponseDto {
    private String exception;
    private String message;
    private LocalDateTime timestamp;
    private List<String> details;

    public String getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
