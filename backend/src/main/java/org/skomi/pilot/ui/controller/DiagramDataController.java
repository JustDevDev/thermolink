package org.skomi.pilot.ui.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.skomi.pilot.ui.model.DiagramDTO;
import org.skomi.pilot.ui.service.DiagramService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing diagram data (PLCs and Sensors).
 */
@RestController
@RequestMapping("/api/diagram/data")
@RequiredArgsConstructor
@Tag(name = "Diagram Data", description = "API endpoints for managing diagram PLCs and Sensors")
public class DiagramDataController {

    private final DiagramService diagramService;

    /**
     * POST endpoint to save diagram data.
     *
     * @param dto            the diagram data to save
     * @param authentication the authentication object containing user details
     * @return ResponseEntity containing the saved diagram data
     */
    @PostMapping
    @Operation(summary = "Save diagram data", description = "Saves or updates PLCs and Sensors data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully saved diagram data"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<DiagramDTO> saveDiagramData(
            @RequestBody DiagramDTO dto,
            Authentication authentication
    ) {
        if (dto == null || dto.PLCs() == null || dto.sensors() == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(diagramService.saveDiagramData(authentication.getName(), dto));
    }
}