package com.epamtask.facade.impl;

import com.epamtask.dto.authenticationdto.AuthTokenResponseDto;
import com.epamtask.dto.authenticationdto.LoginRequestDto;
import com.epamtask.dto.authenticationdto.PasswordChangeRequestDto;

import com.epamtask.mapper.AuthMapper;

import com.epamtask.security.AuthSessionStore;
import com.epamtask.service.AuthenticationService;
import com.epamtask.utils.PasswordGenerator;
import com.epamtask.utils.UserNameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthenticationFacadeImplTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private AuthSessionStore sessionStore;

    @Mock
    private UserNameGenerator userNameGenerator;

    @Mock
    private PasswordGenerator passwordGenerator;

    @Mock
    private AuthMapper authMapper;

    @InjectMocks
    private AuthenticationFacadeImpl authenticationFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_shouldReturnToken() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setUsername("user");
        dto.setPassword("pass");

        when(authenticationService.authenticate("user", "pass")).thenReturn(true);
        when(sessionStore.createToken("user", "pass")).thenReturn("token123");

        AuthTokenResponseDto response = authenticationFacade.login(dto);

        assertEquals("token123", response.getToken());
    }

    @Test
    void changePassword_shouldCallService() {
        PasswordChangeRequestDto dto = new PasswordChangeRequestDto();
        dto.setUsername("user");
        dto.setOldPassword("old");
        dto.setNewPassword("new");

        when(authenticationService.authenticate("user", "old")).thenReturn(true);

        authenticationFacade.changePassword(dto);

        verify(authenticationService).updatePasswordWithoutAuth("user", "new");
    }
}
