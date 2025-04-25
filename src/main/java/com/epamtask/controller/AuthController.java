package com.epamtask.controller;

import com.epamtask.aspect.annotation.MeasureApi;
import com.epamtask.dto.authenticationdto.AuthResponseDto;
import com.epamtask.dto.authenticationdto.AuthTokenResponseDto;
import com.epamtask.dto.authenticationdto.LoginRequestDto;
import com.epamtask.dto.authenticationdto.PasswordChangeRequestDto;
import com.epamtask.dto.traineedto.TraineeRegistrationRequestDto;
import com.epamtask.dto.trainerdto.TrainerRegistrationRequestDto;
import com.epamtask.facade.AuthenticationFacade;
import com.epamtask.service.metrics.ApiMetricsService;
import com.epamtask.service.metrics.AuthenticationMetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.epamtask.config.SwaggerExamplesConfig.LOGIN_EXAMPLE;
import static com.epamtask.config.SwaggerExamplesConfig.PASSWORD_CHANGE_EXAMPLE;
import static com.epamtask.config.SwaggerExamplesConfig.TRAINEE_REGISTRATION_EXAMPLE;
import static com.epamtask.config.SwaggerExamplesConfig.TRAINER_REGISTRATION_EXAMPLE;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "Handles registration, login and password change")
public class AuthController {

    private final AuthenticationFacade authenticationFacade;
    private final AuthenticationMetricsService authMetrics;
    private final ApiMetricsService apiMetrics;

    public AuthController(AuthenticationFacade authenticationFacade,
                          AuthenticationMetricsService authMetrics,
                          ApiMetricsService apiMetrics) {
        this.authenticationFacade = authenticationFacade;
        this.authMetrics = authMetrics;
        this.apiMetrics = apiMetrics;
    }

    @MeasureApi(endpoint = "/api/auth/trainees/register", method = "POST")
    @PostMapping("/trainees/register")
    @Operation(
            summary = "Register new trainee",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema   = @Schema(implementation = TraineeRegistrationRequestDto.class),
                            examples = @ExampleObject(value = TRAINEE_REGISTRATION_EXAMPLE)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee registered",
                            content = @Content(schema = @Schema(implementation = AuthResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
            }
    )
    public ResponseEntity<AuthResponseDto> registerTrainee(
            @Valid @RequestBody TraineeRegistrationRequestDto dto) {
        AuthResponseDto resp = authenticationFacade.registerTrainee(dto);
        authMetrics.success();
        return ResponseEntity.ok(resp);
    }

    @MeasureApi(endpoint = "/api/auth/trainers/register", method = "POST")
    @PostMapping("/trainers/register")
    @Operation(
            summary = "Register new trainer",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema   = @Schema(implementation = TrainerRegistrationRequestDto.class),
                            examples = @ExampleObject(value = TRAINER_REGISTRATION_EXAMPLE)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer registered",
                            content = @Content(schema = @Schema(implementation = AuthResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
            }
    )
    public ResponseEntity<AuthResponseDto> registerTrainer(
            @Valid @RequestBody TrainerRegistrationRequestDto dto) {
        AuthResponseDto resp = authenticationFacade.registerTrainer(dto);
        authMetrics.success();
        return ResponseEntity.ok(resp);
    }

    @MeasureApi(endpoint = "/api/auth/login", method = "POST")
    @PostMapping("/login")
    @Operation(
            summary = "Login user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema   = @Schema(implementation = LoginRequestDto.class),
                            examples = @ExampleObject(value = LOGIN_EXAMPLE)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful",
                            content = @Content(schema = @Schema(implementation = AuthTokenResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content)
            }
    )
    public ResponseEntity<AuthTokenResponseDto> login(
            @Valid @RequestBody LoginRequestDto dto) {
        AuthTokenResponseDto token;
        try {
            token = apiMetrics.trackCallable("/api/auth/login", "POST", 200, 0, () -> authMetrics.timed(() -> authenticationFacade.login(dto)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (token != null) {
            authMetrics.success();
            return ResponseEntity.ok(token);
        } else {
            authMetrics.fail(dto.getUsername());
            authMetrics.lock(dto.getUsername());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    @MeasureApi(endpoint = "/api/auth/change-password", method = "PUT")
    @PutMapping("/change-password")
    @Operation(
            summary = "Change password",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema   = @Schema(implementation = PasswordChangeRequestDto.class),
                            examples = @ExampleObject(value = PASSWORD_CHANGE_EXAMPLE)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password changed"),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
            }
    )
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody PasswordChangeRequestDto dto) {
        authenticationFacade.changePassword(dto);
        authMetrics.success();
        return ResponseEntity.ok().build();
    }
}
