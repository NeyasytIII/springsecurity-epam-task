package com.epamtask.mapper.impl;
import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.dto.authenticationdto.AuthResponseDto;
import com.epamtask.mapper.AuthMapper;
import com.epamtask.model.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapperImpl implements AuthMapper {
    @Loggable
    @Override
    public AuthResponseDto toAuthResponse(User user) {
        AuthResponseDto dto = new AuthResponseDto();
        dto.setUsername(user.getUserName());
        dto.setPassword(user.getPassword());
        return dto;
    }
}