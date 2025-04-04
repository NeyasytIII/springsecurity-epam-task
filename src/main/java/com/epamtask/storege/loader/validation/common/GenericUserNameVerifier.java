package com.epamtask.storege.loader.validation.common;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.Trainer;
import com.epamtask.model.Trainee;
import com.epamtask.utils.UserNameGenerator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Component
public class GenericUserNameVerifier<T> {
    private final UserNameGenerator userNameGenerator;

    public GenericUserNameVerifier(UserNameGenerator userNameGenerator) {
        this.userNameGenerator = userNameGenerator;
    }

    @Loggable
    public List<String> verifyUserNames(List<T> users) {
        List<String> errors = new ArrayList<>();
        Set<Long> uniqueIds = new HashSet<>();
        for (T user : users) {
            Long id = extractId(user);
            if (!uniqueIds.add(id)) {
                errors.add("Duplicate user ID: " + id);
                continue;
            }
            String generated = userNameGenerator.generateUserName(extractFirstName(user), extractLastName(user));
            String actual = extractUserName(user);
            if (!actual.equalsIgnoreCase(generated)) {
                errors.add("UserName mismatch for user ID " + id + ": expected " + generated + " but found " + actual);
            }
        }
        return errors;
    }

    @Loggable
    private Long extractId(T user) {
        if (user instanceof Trainee) {
            return ((Trainee) user).getTraineeId();
        } else if (user instanceof Trainer) {
            return ((Trainer) user).getTrainerId();
        }
        throw new IllegalArgumentException("Unknown user type");
    }

    @Loggable
    private String extractFirstName(T user) {
        if (user instanceof Trainee) {
            return ((Trainee) user).getFirstName();
        } else if (user instanceof Trainer) {
            return ((Trainer) user).getFirstName();
        }
        throw new IllegalArgumentException("Unknown user type");
    }

    @Loggable
    private String extractLastName(T user) {
        if (user instanceof Trainee) {
            return ((Trainee) user).getLastName();
        } else if (user instanceof Trainer) {
            return ((Trainer) user).getLastName();
        }
        throw new IllegalArgumentException("Unknown user type");
    }

    @Loggable
    private String extractUserName(T user) {
        if (user instanceof Trainee) {
            return ((Trainee) user).getUserName();
        } else if (user instanceof Trainer) {
            return ((Trainer) user).getUserName();
        }
        throw new IllegalArgumentException("Unknown user type");
    }
}