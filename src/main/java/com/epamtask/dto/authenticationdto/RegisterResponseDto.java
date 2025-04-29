package com.epamtask.dto.authenticationdto;

public class RegisterResponseDto {
    private String token;
    private String username;
    private String password;

    public RegisterResponseDto(String token, String username, String password) {
        this.token = token;
        this.username = username;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}