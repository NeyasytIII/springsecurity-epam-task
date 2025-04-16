package com.epamtask.dto.authenticationdto;
public class AuthTokenResponseDto {
    private String token;

    public AuthTokenResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}