package com.epamtask.mapper;

import com.epamtask.dto.authenticationdto.AuthResponseDto;
import com.epamtask.model.User;

public interface AuthMapper {
    AuthResponseDto toAuthResponse(User user);
}