package com.epamtask.dto.authenticationdto;

import jakarta.validation.constraints.NotBlank;

public class PasswordChangeRequestDto {
    @NotBlank
    private String username;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;

    public @NotBlank String getUsername() {
        return username;
    }

    public @NotBlank String getOldPassword() {
        return oldPassword;
    }

    public @NotBlank String getNewPassword() {
        return newPassword;
    }

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }

    public void setOldPassword(@NotBlank String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setNewPassword(@NotBlank String newPassword) {
        this.newPassword = newPassword;
    }
}
