package com.epamtask.controller;

import com.epamtask.aspect.annotation.MeasureApi;
import com.epamtask.dto.trainerdto.TrainerProfileResponseDto;
import com.epamtask.dto.trainerdto.TrainerUpdateRequestDto;
import com.epamtask.dto.trainingdto.TrainingResponseDto;
import com.epamtask.exception.NotFoundException;
import com.epamtask.facade.TrainerFacade;
import com.epamtask.facade.TrainingFacade;
import com.epamtask.mapper.TrainerMapper;
import com.epamtask.mapper.TrainingMapper;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import com.epamtask.service.metrics.DatabaseMetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.epamtask.config.SwaggerExamplesConfig.TRAINER_UPDATE_EXAMPLE;

@RestController
@RequestMapping("/api/trainers")
@Tag(name = "Trainer Controller", description = "Handles trainer profile and training history")
public class TrainerController {

    private final TrainerFacade trainerFacade;
    private final TrainingFacade trainingFacade;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;
    private final DatabaseMetricsService dbMetrics;

    public TrainerController(TrainerFacade trainerFacade,
                             TrainingFacade trainingFacade,
                             TrainerMapper trainerMapper,
                             TrainingMapper trainingMapper,
                             DatabaseMetricsService dbMetrics) {
        this.trainerFacade = trainerFacade;
        this.trainingFacade = trainingFacade;
        this.trainerMapper = trainerMapper;
        this.trainingMapper = trainingMapper;
        this.dbMetrics = dbMetrics;
    }

    @MeasureApi(endpoint = "/api/trainers/{username}/profile", method = "GET")
    @GetMapping("/{username}/profile")
    @Operation(summary = "Get trainer profile",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile found",
                            content = @Content(schema = @Schema(implementation = TrainerProfileResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
            })
    public ResponseEntity<TrainerProfileResponseDto> getProfile(@PathVariable String username) {
        try {
            TrainerProfileResponseDto dto = dbMetrics.trackCallable("trainer", "select", () -> trainerFacade.getTrainerProfile(username));
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @MeasureApi(endpoint = "/api/trainers/{username}", method = "PUT")
    @PutMapping("/{username}")
    @Operation(summary = "Update trainer profile",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TrainerUpdateRequestDto.class),
                            examples = @ExampleObject(value = TRAINER_UPDATE_EXAMPLE)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile updated",
                            content = @Content(schema = @Schema(implementation = TrainerProfileResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
            })
    public ResponseEntity<TrainerProfileResponseDto> updateProfile(
            @PathVariable String username,
            @RequestBody @Valid TrainerUpdateRequestDto dto) {
        try {
            return dbMetrics.trackCallable("trainer", "update", () -> {
                Trainer existing = trainerFacade.getTrainerByUsername(username)
                        .orElseThrow(() -> new NotFoundException("Trainer not found: " + username));
                Trainer updated = trainerMapper.toEntity(dto, existing);
                updated.setUserName(username);
                trainerFacade.updateTrainer(updated);
                return ResponseEntity.ok(trainerMapper.toProfileDto(updated));
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @MeasureApi(endpoint = "/api/trainers/{username}/status", method = "PATCH")
    @PatchMapping("/{username}/status")
    @Operation(summary = "Toggle trainer activation",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status toggled"),
                    @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
            })
    public ResponseEntity<Void> toggleActivation(@PathVariable String username) {
        try {
            return dbMetrics.trackCallable("trainer", "update", () -> {
                Trainer trainer = trainerFacade.getTrainerByUsername(username)
                        .orElseThrow(() -> new NotFoundException("Trainer not found: " + username));
                if (trainer.isActive()) {
                    trainerFacade.deactivateUser(username);
                } else {
                    trainerFacade.activateUser(username);
                }
                return ResponseEntity.ok().build();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @MeasureApi(endpoint = "/api/trainers/{username}/trainings", method = "GET")
    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get trainer trainings list",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainings found",
                            content = @Content(schema = @Schema(implementation = TrainingResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
            })
    public ResponseEntity<List<TrainingResponseDto>> getTrainerTrainings(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodTo,
            @RequestParam(required = false) String traineeName) {
        try {
            List<Training> trainings = dbMetrics.trackCallable("training", "select", () ->
                    trainingFacade.getTrainingsByTrainerUsernameAndCriteria(
                            username, periodFrom, periodTo, traineeName));
            return ResponseEntity.ok(trainingMapper.toDtoList(trainings));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
