package org.skomi.pilot.ui.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.skomi.pilot.auth.service.JwtTokenService;
import org.skomi.pilot.shared.service.CookiesService;
import org.skomi.pilot.ui.model.SensorTableResponse;
import org.skomi.pilot.ui.model.TableResponse;
import org.skomi.pilot.ui.service.SensorTableService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing sensor-related operations within the UI module.
 * Provides endpoints for retrieving sensor data.
 */
@RestController
@RequestMapping("/api/sensors")
@Tag(name = "Sensor Controller")
@RequiredArgsConstructor
public class SensorController {

    private final SensorTableService sensorTableService;
    private final CookiesService cookiesService;
    private final JwtTokenService jwtTokenService;

    /**
     * Fetch a paginated list of sensors.
     *
     * @param pageable the pagination information provided by the client
     * @return a paginated response containing a list of sensors
     */
    @Operation(summary = "Retrieve a paginated list of sensors.")
    @GetMapping
    public ResponseEntity<TableResponse<SensorTableResponse>> getSensors(Pageable pageable, HttpServletRequest request) {

        String token = cookiesService.extractJwtFromCookies(request);
        String userEmail = jwtTokenService.getUserEmailFromToken(token);

        return ResponseEntity.ok(sensorTableService.getSensorsOnUser(pageable, userEmail));
    }
}