package com.epamtask.storege.loader.validation.uservalidation;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.Trainer;

import org.springframework.stereotype.Component;
import java.util.List;
import static java.util.stream.Collectors.*;

@Component
public class TrainerValidator {
    @Loggable
    public List<String> validate(List<Trainer> trainers) {
        return trainers.stream()
                .flatMap(t -> new UserValidationBuilder<Trainer>()
                        .addRule(tr -> tr.getTrainerId() != null && tr.getTrainerId() > 0, "Invalid trainer ID")
                        .addRule(tr -> tr.getFirstName() != null && !tr.getFirstName().isBlank(), "First name is invalid")
                        .addRule(tr -> tr.getLastName() != null && !tr.getLastName().isBlank(), "Last name is invalid")
                        .addRule(tr -> tr.getUserName() != null && !tr.getUserName().isBlank(), "UserName is invalid")
                        .addRule(tr -> tr.getPassword() != null && !tr.getPassword().isBlank(), "Password is invalid")
                        .addRule(tr -> tr.getSpecialization() != null && !tr.getSpecialization().isBlank(), "Specialization is invalid")
                        .validate(t).stream())
                .collect(toList());
    }
}