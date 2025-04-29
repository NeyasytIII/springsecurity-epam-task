package com.epamtask.controller;

import com.epamtask.aspect.annotation.MeasureApi;
import com.epamtask.dto.trainingdto.TrainingCreateRequestDto;
import com.epamtask.facade.TrainingFacade;
import com.epamtask.mapper.TrainingMapper;
import com.epamtask.service.metrics.TrainingMetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.epamtask.config.SwaggerExamplesConfig.TRAINING_ADD_EXAMPLE;

@RestController
@RequestMapping("/api/training")
@Tag(name = "Training Controller", description = "Handles training creation")
public class TrainingController {

    private final TrainingFacade trainingFacade;
    private final TrainingMapper trainingMapper;
    private final TrainingMetricsService trainingMetrics;

    public TrainingController(TrainingFacade trainingFacade,
                              TrainingMapper trainingMapper,
                              TrainingMetricsService trainingMetrics) {
        this.trainingFacade = trainingFacade;
        this.trainingMapper = trainingMapper;
        this.trainingMetrics = trainingMetrics;
    }

    @MeasureApi(endpoint = "/api/training", method = "POST")
    @PostMapping
    @Operation(
            summary = "Add new training",
            description = "Requires prior call to /api/auth/login to authenticate user.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TrainingCreateRequestDto.class),
                            examples = {@ExampleObject(value = TRAINING_ADD_EXAMPLE)}
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Training added"),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Trainee or Trainer not found", content = @Content)
            }
    )
    public ResponseEntity<Void> addTraining(@Valid @RequestBody TrainingCreateRequestDto dto) {
        trainingFacade.createTrainingFromDto(dto);
        trainingMetrics.created(dto.getTrainingType().name(), dto.getTrainerUsername());
        long durationMinutes = Long.parseLong(dto.getTrainingDuration());
        trainingMetrics.duration(dto.getTrainingType().name(), durationMinutes);
        trainingMetrics.cascade(2);
        return ResponseEntity.ok().build();
    }
}