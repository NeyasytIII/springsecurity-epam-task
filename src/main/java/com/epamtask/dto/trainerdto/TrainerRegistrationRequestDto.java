package com.epamtask.dto.trainerdto;

import jakarta.validation.constraints.NotBlank;

public class TrainerRegistrationRequestDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String specialization;

    public @NotBlank String getFirstName() {
        return firstName;
    }

    public @NotBlank String getLastName() {
        return lastName;
    }

    public @NotBlank String getSpecialization() {
        return specialization;
    }

    public void setFirstName(@NotBlank String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@NotBlank String lastName) {
        this.lastName = lastName;
    }

    public void setSpecialization(@NotBlank String specialization) {
        this.specialization = specialization;
    }
}
