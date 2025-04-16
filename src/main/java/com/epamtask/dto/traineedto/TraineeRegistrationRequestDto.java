package com.epamtask.dto.traineedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class TraineeRegistrationRequestDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdayDate;

    public @NotBlank String getFirstName() {
        return firstName;
    }

    public @NotBlank String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public Date getBirthdayDate() {
        return birthdayDate;
    }

    public void setFirstName(@NotBlank String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@NotBlank String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthdayDate(Date birthdayDate) {
        this.birthdayDate = birthdayDate;
    }
}
