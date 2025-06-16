package org.skomi.pilot.ui.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.skomi.pilot.ui.model.DiagramSourceDto;
import org.skomi.pilot.ui.service.DiagramSourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing diagram source data.
 */
@RestController
@RequestMapping("/api/diagram/source")
@RequiredArgsConstructor
@Tag(name = "Diagram Source", description = "API endpoints for managing diagram source")
public class DiagramSourceController {

    private final DiagramSourceService diagramSourceService;

    /**
     * GET endpoint to retrieve the diagram source.
     *
     * @return ResponseEntity containing the diagram source data
     */
    @GetMapping
    @Operation(summary = "Get diagram source", description = "Retrieves the current diagram source data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved diagram source"),
            @ApiResponse(responseCode = "404", description = "Diagram source not found")
    })
    public ResponseEntity<DiagramSourceDto> getDiagramSource(Authentication authentication) {
        return diagramSourceService.getDiagram(authentication.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    /**
     * POST endpoint to save new diagram source data.
     *
     * @param dto the diagram source data to save
     * @return ResponseEntity containing the saved diagram source data
     */
    @PostMapping
    @Operation(summary = "Save diagram source", description = "Saves new diagram source data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully saved diagram source"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<DiagramSourceDto> saveDiagramSource(
            @RequestBody DiagramSourceDto dto,
            Authentication authentication
    ) {
        if (dto.diagram() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                diagramSourceService.saveDiagram(authentication.getName(), dto)
        );
    }
}