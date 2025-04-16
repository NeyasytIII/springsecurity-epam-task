package com.epamtask.controller;

import com.epamtask.aspect.annotation.Authenticated;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/api/trainers")
@Tag(name = "Trainer Controller", description = "Handles trainer profile and training history")
public class TrainerController {

    private final TrainerFacade trainerFacade;
    private final TrainingFacade trainingFacade;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;

    public TrainerController(
            TrainerFacade trainerFacade,
            TrainingFacade trainingFacade,
            TrainerMapper trainerMapper,
            TrainingMapper trainingMapper
    ) {
        this.trainerFacade = trainerFacade;
        this.trainingFacade = trainingFacade;
        this.trainerMapper = trainerMapper;
        this.trainingMapper = trainingMapper;
    }

    @GetMapping("/{username}/profile")
    @Authenticated
    @Operation(summary = "Get trainer profile",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile found",
                            content = @Content(schema = @Schema(implementation = TrainerProfileResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
            })
    public ResponseEntity<TrainerProfileResponseDto> getProfile(@PathVariable String username) {
        return ResponseEntity.ok(trainerFacade.getTrainerProfile(username));
    }

    @PutMapping("/{username}")
    @Authenticated
    @Operation(summary = "Update trainer profile",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = TrainerUpdateRequestDto.class))
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

        Trainer existing = trainerFacade.getTrainerByUsername(username)
                .orElseThrow(() -> new NotFoundException("Trainer not found: " + username));

        Trainer updated = trainerMapper.toEntity(dto, existing);
        updated.setUserName(username);
        trainerFacade.updateTrainer(updated);

        return ResponseEntity.ok(trainerMapper.toProfileDto(updated));
    }

    @PatchMapping("/{username}/status")
    @Authenticated
    @Operation(summary = "Toggle trainer activation",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status toggled"),
                    @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
            })
    public ResponseEntity<Void> toggleActivation(@PathVariable String username) {
        Trainer trainer = trainerFacade.getTrainerByUsername(username)
                .orElseThrow(() -> new NotFoundException("Trainer not found: " + username));

        if (trainer.isActive()) {
            trainerFacade.deactivateUser(username);
        } else {
            trainerFacade.activateUser(username);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/trainings")
    @Authenticated
    @Operation(summary = "Get trainer trainings list",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainings found",
                            content = @Content(schema = @Schema(implementation = TrainingResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
            })
    public ResponseEntity<List<TrainingResponseDto>> getTrainerTrainings(
            @PathVariable String username,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            Date periodFrom,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            Date periodTo,
            @RequestParam(required = false) String traineeName
    ) {
        List<Training> trainings = trainingFacade.getTrainingsByTrainerUsernameAndCriteria(
                username, periodFrom, periodTo, traineeName
        );
        return ResponseEntity.ok(trainingMapper.toDtoList(trainings));
    }
}