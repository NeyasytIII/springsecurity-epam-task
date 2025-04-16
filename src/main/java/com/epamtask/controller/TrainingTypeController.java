package com.epamtask.controller;

import com.epamtask.dto.trainingdto.TrainingTypeResponseDto;
import com.epamtask.facade.TrainingTypeFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/training-types")
@Tag(name = "Training Type Controller", description = "Provides training type data")
public class TrainingTypeController {

    private final TrainingTypeFacade trainingTypeFacade;

    public TrainingTypeController(TrainingTypeFacade trainingTypeFacade) {
        this.trainingTypeFacade = trainingTypeFacade;
    }

    @GetMapping
    @Operation(
            summary = "Get all training types",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of training types",
                            content = @Content(schema = @Schema(implementation = TrainingTypeResponseDto.class)))
            }
    )
    public ResponseEntity<List<TrainingTypeResponseDto>> getAllTrainingTypes() {
        return ResponseEntity.ok(trainingTypeFacade.getAllTrainingTypes());
    }
}