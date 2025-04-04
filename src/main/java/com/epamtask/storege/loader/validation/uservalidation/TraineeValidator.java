package com.epamtask.storege.loader.validation.uservalidation;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.Trainee;
import com.epamtask.storege.loader.validation.common.DateValidator;

import org.springframework.stereotype.Component;
import java.util.List;
import static java.util.stream.Collectors.*;

@Component
public class TraineeValidator {
    private final DateValidator dateValidator;

    public TraineeValidator(DateValidator dateValidator) {
        this.dateValidator = dateValidator;
    }

    @Loggable
    public List<String> validate(List<Trainee> trainees) {
        return trainees.stream()
                .flatMap(t -> new UserValidationBuilder<Trainee>()
                        .addRule(tr -> tr.getTraineeId() != null && tr.getTraineeId() > 0, "Invalid trainee ID")
                        .addRule(tr -> tr.getFirstName() != null && !tr.getFirstName().isBlank(), "First name is invalid")
                        .addRule(tr -> tr.getLastName() != null && !tr.getLastName().isBlank(), "Last name is invalid")
                        .addRule(tr -> tr.getUserName() != null && !tr.getUserName().isBlank(), "UserName is invalid")
                        .addRule(tr -> tr.getPassword() != null && !tr.getPassword().isBlank(), "Password is invalid")
                        .addRule(tr -> dateValidator.isDateValid(tr.getBirthdayDate()), "Invalid or missing birthday date")
                        .validate(t).stream())
                .collect(toList());
    }
}