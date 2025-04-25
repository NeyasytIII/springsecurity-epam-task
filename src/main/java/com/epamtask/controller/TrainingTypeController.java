package com.epamtask.controller;

import com.epamtask.aspect.annotation.MeasureApi;
import com.epamtask.dto.trainingdto.TrainingTypeResponseDto;
import com.epamtask.facade.TrainingTypeFacade;
import com.epamtask.service.metrics.DatabaseMetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-types")
@Tag(name = "Training Type Controller", description = "Provides training type data")
public class TrainingTypeController {

    private final TrainingTypeFacade trainingTypeFacade;
    private final DatabaseMetricsService dbMetrics;

    public TrainingTypeController(TrainingTypeFacade trainingTypeFacade,
                                  DatabaseMetricsService dbMetrics) {
        this.trainingTypeFacade = trainingTypeFacade;
        this.dbMetrics = dbMetrics;
    }

    @MeasureApi(endpoint = "/api/training-types", method = "GET")
    @GetMapping
    @Operation(
            summary = "Get all training types",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of training types",
                            content = @Content(schema = @Schema(implementation = TrainingTypeResponseDto.class)))
            }
    )
    public ResponseEntity<List<TrainingTypeResponseDto>> getAllTrainingTypes() {
        List<TrainingTypeResponseDto> list;
        try {
            list = dbMetrics.trackCallable("training_type", "select", trainingTypeFacade::getAllTrainingTypes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(list);
    }
}