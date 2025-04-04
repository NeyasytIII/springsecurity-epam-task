package com.epamtask.storege.loader.validation.uservalidation;

import com.epamtask.aspect.annotation.Loggable;

import java.util.function.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UserValidationBuilder<T> {
    private final List<Rule<T>> rules = new ArrayList<>();

    @Loggable
    public UserValidationBuilder<T> addRule(Predicate<T> predicate, String errorMessage) {
        rules.add(new Rule<>(predicate, errorMessage));
        return this;
    }
    @Loggable
    public List<String> validate(T entity) {
        List<String> errors = new ArrayList<>();
        for (Rule<T> rule : rules) {
            if (!rule.predicate.test(entity)) {
                errors.add(rule.errorMessage);
            }
        }
        return errors;
    }
    private static class Rule<T> {
        private final Predicate<T> predicate;
        private final String errorMessage;
        public Rule(Predicate<T> predicate, String errorMessage) {
            this.predicate = predicate;
            this.errorMessage = errorMessage;
        }
    }
}