package com.epamtask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.models.GroupedOpenApi;

@Configuration
public class SwaggerExamplesConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/**")
                .build();
    }

    public static final String LOGIN_EXAMPLE = """
        {
          "username": "Pam.Beesly",
          "password": "pam321"
        }
    """;

    public static final String PASSWORD_CHANGE_EXAMPLE = """
        {
          "username": "Pam.Beesly",
          "oldPassword": "pam321",
          "newPassword": "newpass999"
        }
    """;

    public static final String TRAINEE_REGISTRATION_EXAMPLE = """
        {
          "firstName": "Alice",
          "lastName": "Brown",
          "address": "123 Main St",
          "birthdayDate": "1990-01-01"
        }
    """;

    public static final String TRAINER_REGISTRATION_EXAMPLE = """
        {
          "firstName": "Michael",
          "lastName": "Scott",
          "specialization": "STRENGTH"
        }
    """;

    public static final String TRAINEE_UPDATE_EXAMPLE = """
        {
          "firstName": "Pamela",
          "lastName": "Beesly",
          "address": "321 New St",
          "birthdayDate": "1991-02-14",
          "isActive": true
        }
    """;

    public static final String TRAINER_UPDATE_EXAMPLE = """
        {
          "firstName": "Michael",
          "lastName": "Scott",
          "isActive": true
        }
    """;

    public static final String TRAINEE_TRAINERS_UPDATE = """
        {
          "traineeUsername": "Pam.Beesly",
          "trainerUsernames": ["Michael.Scott", "John.Doe"]
        }
    """;

    public static final String TRAINING_ADD_EXAMPLE = """
        {
          "traineeUsername": "Pam.Beesly",
          "trainerUsername": "Michael.Scott",
          "trainingName": "Leg Day",
          "trainingDate": "2024-05-01",
          "trainingDuration": "60m",
          "trainingType": "STRENGTH"
        }
    """;

    public static final String FILTER_TRAININGS_TRAINEE = """
        {
          "username": "Pam.Beesly",
          "periodFrom": "2024-01-01",
          "periodTo": "2024-06-01",
          "trainerName": "Michael",
          "trainingType": "STRENGTH"
        }
    """;

    public static final String FILTER_TRAININGS_TRAINER = """
        {
          "username": "Michael.Scott",
          "periodFrom": "2024-01-01",
          "periodTo": "2024-06-01",
          "traineeName": "Pam"
        }
    """;
}