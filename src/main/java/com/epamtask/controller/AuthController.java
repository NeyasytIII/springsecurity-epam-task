package com.epamtask.controller;

import com.epamtask.dto.authenticationdto.*;
import com.epamtask.dto.traineedto.TraineeRegistrationRequestDto;
import com.epamtask.dto.trainerdto.TrainerRegistrationRequestDto;
import com.epamtask.facade.AuthenticationFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.epamtask.config.SwaggerExamplesConfig.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "Handles registration, login and password change")
public class AuthController {

    private final AuthenticationFacade authenticationFacade;

    public AuthController(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    @PostMapping("/trainees/register")
    @Operation(
            summary = "Register new trainee",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TraineeRegistrationRequestDto.class),
                            examples = {@ExampleObject(value = TRAINEE_REGISTRATION_EXAMPLE)}
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee registered",
                            content = @Content(schema = @Schema(implementation = AuthResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
            }
    )
    public ResponseEntity<AuthResponseDto> registerTrainee(@Valid @RequestBody TraineeRegistrationRequestDto dto) {
        return ResponseEntity.ok(authenticationFacade.registerTrainee(dto));
    }

    @PostMapping("/trainers/register")
    @Operation(
            summary = "Register new trainer",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TrainerRegistrationRequestDto.class),
                            examples = {@ExampleObject(value = TRAINER_REGISTRATION_EXAMPLE)}
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer registered",
                            content = @Content(schema = @Schema(implementation = AuthResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
            }
    )
    public ResponseEntity<AuthResponseDto> registerTrainer(@Valid @RequestBody TrainerRegistrationRequestDto dto) {
        return ResponseEntity.ok(authenticationFacade.registerTrainer(dto));
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LoginRequestDto.class),
                            examples = {@ExampleObject(value = LOGIN_EXAMPLE)}
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful",
                            content = @Content(schema = @Schema(implementation = AuthTokenResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content)
            }
    )
    public ResponseEntity<AuthTokenResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(authenticationFacade.login(dto));
    }

    @PutMapping("/change-password")
    @Operation(
            summary = "Change password",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PasswordChangeRequestDto.class),
                            examples = {@ExampleObject(value = PASSWORD_CHANGE_EXAMPLE)}
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password changed"),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
            }
    )
    public ResponseEntity<Void> changePassword(@Valid @RequestBody PasswordChangeRequestDto dto) {
        authenticationFacade.changePassword(dto);
        return ResponseEntity.ok().build();
    }
}