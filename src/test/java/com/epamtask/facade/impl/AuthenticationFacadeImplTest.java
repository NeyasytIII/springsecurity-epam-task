package com.epamtask.facade.impl;

import com.epamtask.dto.authenticationdto.AuthTokenResponseDto;
import com.epamtask.dto.authenticationdto.LoginRequestDto;
import com.epamtask.dto.authenticationdto.RegisterResponseDto;
import com.epamtask.dto.traineedto.TraineeRegistrationRequestDto;
import com.epamtask.dto.trainerdto.TrainerRegistrationRequestDto;
import com.epamtask.facade.TraineeFacade;
import com.epamtask.facade.TrainerFacade;
import com.epamtask.security.JwtTokenProvider;
import com.epamtask.service.AuthenticationService;
import com.epamtask.service.LogoutService;
import com.epamtask.service.impl.BruteForceProtectionService;
import com.epamtask.utils.UserNameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationFacadeImplTest {

    @Mock
    private TraineeFacade traineeFacade;

    @Mock
    private TrainerFacade trainerFacade;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserNameGenerator userNameGenerator;

    @Mock
    private BruteForceProtectionService bruteForceProtectionService;

    @Mock
    private LogoutService logoutService;

    @InjectMocks
    private AuthenticationFacadeImpl authenticationFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerTrainee_shouldReturnRegisterResponse() {
        TraineeRegistrationRequestDto dto = new TraineeRegistrationRequestDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setAddress("Street");
        dto.setBirthdayDate(new java.util.Date());

        when(userNameGenerator.generateUserName("John", "Doe")).thenReturn("John.Doe");
        when(jwtTokenProvider.generateToken("John.Doe")).thenReturn("token123");

        RegisterResponseDto response = authenticationFacade.registerTrainee(dto);

        assertNotNull(response);
        assertEquals("John.Doe", response.getUsername());
        assertEquals("token123", response.getToken());
        verify(traineeFacade).createTrainee(any(), any(), any(), any());
        verify(authenticationService).register(eq("John.Doe"), any(), eq("TRAINEE"));
    }

    @Test
    void registerTrainer_shouldReturnRegisterResponse() {
        TrainerRegistrationRequestDto dto = new TrainerRegistrationRequestDto();
        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setSpecialization("STRENGTH");

        when(userNameGenerator.generateUserName("Jane", "Smith")).thenReturn("Jane.Smith");
        when(jwtTokenProvider.generateToken("Jane.Smith")).thenReturn("token456");

        RegisterResponseDto response = authenticationFacade.registerTrainer(dto);

        assertNotNull(response);
        assertEquals("Jane.Smith", response.getUsername());
        assertEquals("token456", response.getToken());
        verify(trainerFacade).createTrainer(any(), any(), any());
        verify(authenticationService).register(eq("Jane.Smith"), any(), eq("TRAINER"));
    }

    @Test
    void login_successful_shouldReturnToken() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setUsername("user");
        dto.setPassword("pass");

        when(bruteForceProtectionService.isLocked("user")).thenReturn(false);
        when(authenticationService.authenticate("user", "pass")).thenReturn(true);
        when(jwtTokenProvider.generateToken("user")).thenReturn("jwt-token");

        AuthTokenResponseDto response = authenticationFacade.login(dto);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        verify(bruteForceProtectionService).resetAttempts("user");
    }

    @Test
    void login_lockedUser_shouldThrowException() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setUsername("lockedUser");
        dto.setPassword("pass");

        when(bruteForceProtectionService.isLocked("lockedUser")).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> authenticationFacade.login(dto));
        verify(authenticationService, never()).authenticate(any(), any());
    }

    @Test
    void login_invalidCredentials_shouldThrowException() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setUsername("user");
        dto.setPassword("wrong");

        when(bruteForceProtectionService.isLocked("user")).thenReturn(false);
        when(authenticationService.authenticate("user", "wrong")).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> authenticationFacade.login(dto));
        verify(bruteForceProtectionService).recordFailedAttempt("user");
    }

    @Test
    void changePassword_shouldCallService() {
        authenticationFacade.changePassword("user", "oldPass", "newPass");
        verify(authenticationService).changePassword("user", "oldPass", "newPass");
    }

    @Test
    void logout_validHeader_shouldCallLogout() {
        String token = "Bearer abc.def.ghi";

        authenticationFacade.logout(token);

        verify(logoutService).logout("abc.def.ghi");
    }

    @Test
    void logout_invalidHeader_shouldThrowException() {
        String invalidHeader = "InvalidToken";

        assertThrows(ResponseStatusException.class, () -> authenticationFacade.logout(invalidHeader));
        verifyNoInteractions(logoutService);
    }
}