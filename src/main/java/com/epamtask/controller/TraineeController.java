package com.epamtask.controller;

import com.epamtask.aspect.annotation.Authenticated;
import com.epamtask.dto.traineedto.TraineeProfileResponseDto;
import com.epamtask.dto.traineedto.TraineeTrainerUpdateDto;
import com.epamtask.dto.traineedto.TraineeUpdateRequestDto;
import com.epamtask.dto.trainerdto.TrainerShortDto;
import com.epamtask.dto.trainingdto.TrainingResponseDto;
import com.epamtask.exception.NotFoundException;
import com.epamtask.facade.TraineeFacade;
import com.epamtask.facade.TrainerFacade;
import com.epamtask.facade.TrainingFacade;
import com.epamtask.mapper.TraineeMapper;
import com.epamtask.mapper.TrainerMapper;
import com.epamtask.mapper.TrainingMapper;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.epamtask.config.SwaggerExamplesConfig.TRAINEE_UPDATE_EXAMPLE;
import static com.epamtask.config.SwaggerExamplesConfig.TRAINEE_TRAINERS_UPDATE;

@RestController
@RequestMapping("/api/trainees")
@Tag(name = "Trainee Controller", description = "Handles trainee profile and trainer assignment")
public class TraineeController {

    private final TraineeFacade traineeFacade;
    private final TrainerFacade trainerFacade;
    private final TrainingFacade trainingFacade;
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;

    public TraineeController(
            TraineeFacade traineeFacade,
            TrainerFacade trainerFacade,
            TrainingFacade trainingFacade,
            TraineeMapper traineeMapper,
            TrainerMapper trainerMapper,
            TrainingMapper trainingMapper
    ) {
        this.traineeFacade = traineeFacade;
        this.trainerFacade = trainerFacade;
        this.trainingFacade = trainingFacade;
        this.traineeMapper = traineeMapper;
        this.trainerMapper = trainerMapper;
        this.trainingMapper = trainingMapper;
    }

    @GetMapping("/{username}/profile")
    @Authenticated
    @Operation(summary = "Get trainee profile", responses = {
            @ApiResponse(responseCode = "200", description = "Profile found",
                    content = @Content(schema = @Schema(implementation = TraineeProfileResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    public ResponseEntity<TraineeProfileResponseDto> getProfile(@PathVariable String username) {
        return ResponseEntity.ok(traineeFacade.getTraineeProfile(username));
    }

    @PutMapping("/{username}")
    @Authenticated
    @Operation(
            summary = "Update trainee profile",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TraineeUpdateRequestDto.class),
                            examples = {@ExampleObject(value = TRAINEE_UPDATE_EXAMPLE)}
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile updated",
                            content = @Content(schema = @Schema(implementation = TraineeProfileResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
            }
    )
    public ResponseEntity<TraineeProfileResponseDto> updateProfile(
            @PathVariable String username,
            @RequestBody @Valid TraineeUpdateRequestDto dto) {

        Trainee existing = traineeFacade.getTraineeByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found: " + username));
        Trainee updated = traineeMapper.toEntity(dto, existing);
        updated.setUserName(username);
        traineeFacade.updateTrainee(updated);
        return ResponseEntity.ok(traineeMapper.toProfileDto(updated));
    }

    @DeleteMapping("/{username}")
    @Authenticated
    @Operation(summary = "Delete trainee profile", responses = {
            @ApiResponse(responseCode = "200", description = "Trainee deleted"),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    public ResponseEntity<Void> deleteTrainee(@PathVariable String username) {
        traineeFacade.deleteTrainee(username);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{username}/status")
    @Authenticated
    @Operation(
            summary = "Toggle trainee activation",
            description = "Switches the trainee's active status to the opposite",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status toggled"),
                    @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
            }
    )
    public ResponseEntity<Void> toggleActivation(@PathVariable String username) {
        Trainee trainee = traineeFacade.getTraineeByUsername(username)
                .orElseThrow(() -> new NotFoundException("Trainee not found: " + username));

        if (trainee.isActive()) {
            traineeFacade.deactivateTrainee(username);
        } else {
            traineeFacade.activateTrainee(username);
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/trainers")
    @Authenticated
    @Operation(
            summary = "Assign trainers to trainee",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TraineeTrainerUpdateDto.class),
                            examples = {@ExampleObject(value = TRAINEE_TRAINERS_UPDATE)}
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainers updated",
                            content = @Content(schema = @Schema(implementation = TrainerShortDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
            }
    )
    public ResponseEntity<List<TrainerShortDto>> updateTrainers(
            @RequestBody @Valid TraineeTrainerUpdateDto dto) {
        traineeFacade.assignTrainersToTrainee(dto.getTraineeUsername(), dto.getTrainerUsernames());
        Trainee trainee = traineeFacade.getTraineeByUsername(dto.getTraineeUsername())
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found"));
        return ResponseEntity.ok(traineeMapper.toProfileDto(trainee).getTrainers());
    }

    @GetMapping("/{username}/free-trainers")
    @Authenticated
    @Operation(summary = "Get free trainers not assigned via trainings", responses = {
            @ApiResponse(responseCode = "200", description = "Trainers fetched",
                    content = @Content(schema = @Schema(implementation = TrainerShortDto.class))),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    public ResponseEntity<List<TrainerShortDto>> getFreeTrainers(@PathVariable String username) {
        List<TrainerShortDto> free = trainerFacade.getFreeTrainersNotAssignedByTrainings(username);
        return ResponseEntity.ok(free);
    }

    @GetMapping("/{username}/trainings")
    @Authenticated
    @Operation(
            summary = "Get trainee trainings by filters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainings found",
                            content = @Content(schema = @Schema(implementation = TrainingResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
            }
    )
    public ResponseEntity<List<TrainingResponseDto>> getTraineeTrainings(
            @PathVariable String username,
            @RequestParam(required = false) @Schema(description = "Filter from this date (yyyy-MM-dd)")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            Date periodFrom,
            @RequestParam(required = false) @Schema(description = "Filter to this date (yyyy-MM-dd)")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            Date periodTo,
            @RequestParam(required = false) @Schema(description = "Filter by trainer username") String trainerName,
            @RequestParam(required = false) @Schema(description = "Filter by training type") String trainingType
    ) {
        List<Training> trainings = trainingFacade.getTrainingsByTraineeUsernameAndCriteria(
                username,
                periodFrom,
                periodTo,
                trainerName,
                trainingType
        );
        return ResponseEntity.ok(trainingMapper.toDtoList(trainings));
    }
}