package com.epamtask.storege.loader.validation.common;

import com.epamtask.aspect.annotation.Loggable;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DateValidator {
    @Loggable
    public boolean isDateValid(Date date) {
        return date != null && date.before(new Date());
    }
}