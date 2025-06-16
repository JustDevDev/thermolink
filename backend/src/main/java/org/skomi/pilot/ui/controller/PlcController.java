package org.skomi.pilot.ui.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.skomi.pilot.auth.service.JwtTokenService;
import org.skomi.pilot.shared.service.CookiesService;
import org.skomi.pilot.ui.model.PlcTableResponse;
import org.skomi.pilot.ui.model.TableResponse;
import org.skomi.pilot.ui.service.PlcTableService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Programmable Logic Controller (PLC) entities.
 * It provides endpoints to view and interact with PLC data.
 */
@RestController
@RequestMapping("/api/plcs")
@RequiredArgsConstructor
@Tag(name = "PLC Controller", description = "Provides APIs to manage PLC entities.")
public class PlcController {

    private final PlcTableService plcService;
    private final CookiesService cookiesService;
    private final JwtTokenService jwtTokenService;

    /**
     * Retrieves a paginated list of PLC entities.
     *
     * @param pageable the pagination information
     * @return a response containing a page of PLC entities
     */
    @Operation(summary = "Get all PLCs", description = "Fetch a paginated list of all PLC entities.")
    @GetMapping
    public ResponseEntity<TableResponse<PlcTableResponse>> getPlcs(Pageable pageable, HttpServletRequest request) {

        String token = cookiesService.extractJwtFromCookies(request);
        String userEmail = jwtTokenService.getUserEmailFromToken(token);

        return ResponseEntity.ok(plcService.getPlcs(pageable, userEmail));
    }
}