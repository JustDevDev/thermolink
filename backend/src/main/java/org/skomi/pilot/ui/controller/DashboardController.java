package org.skomi.pilot.ui.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.skomi.pilot.auth.service.JwtTokenService;
import org.skomi.pilot.shared.model.DashboardContinentDto;
import org.skomi.pilot.shared.model.DashboardKpisDto;
import org.skomi.pilot.shared.model.DashboardPlcsDto;
import org.skomi.pilot.shared.model.User;
import org.skomi.pilot.shared.repository.UserRepository;
import org.skomi.pilot.shared.service.CookiesService;
import org.skomi.pilot.ui.model.DashboardPlcWithSensorsDto;
import org.skomi.pilot.ui.service.DashboardKpiService;
import org.skomi.pilot.ui.service.DashboardPlcService;
import org.skomi.pilot.ui.service.DashboardSensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller responsible for handling dashboard-related operations.
 * Provides endpoints for retrieving PLCs, sensor data, and continent-based analytics.
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard controller", description = "Provides APIs to manage Dashboard entities.")
public class DashboardController {

    private final CookiesService cookiesService;
    private final JwtTokenService jwtTokenService;
    private final DashboardPlcService dashboardPlcService;
    private final UserRepository userRepository;
    private final DashboardSensorService dashboardSensorService;
    private final DashboardKpiService dashboardKpiService;

    /**
     * Retrieves a list of all PLCs associated with the authenticated user.
     *
     * @param request the HttpServletRequest containing the client's cookies and other request details
     * @return ResponseEntity containing a list of DashboardPlcsDto objects representing the PLCs
     */
    @Operation(summary = "Get all PLCs", description = "Fetch a paginated list of all PLC entities.")
    @GetMapping(path = "/plcs")
    public ResponseEntity<List<DashboardPlcsDto>> getPlcInfos(HttpServletRequest request) {

        String token = cookiesService.extractJwtFromCookies(request);
        String userEmail = jwtTokenService.getUserEmailFromToken(token);
        User user = userRepository.findByUserEmail(userEmail).orElseThrow();

        return ResponseEntity.ok(dashboardPlcService.getAllPlcs(user.getId()));
    }

    /**
     * Retrieves detailed information about a specific PLC and its associated sensors.
     *
     * @param request the HttpServletRequest containing the client's cookies and other request details
     * @param plcId   the unique identifier of the PLC to retrieve
     * @return ResponseEntity containing a DashboardPlcWithSensorsDto object with the PLC and sensor information
     */
    @GetMapping(path = "/" +
            "charts/plc/{id}")
    public ResponseEntity<DashboardPlcWithSensorsDto> getPlcChartInfos(
            HttpServletRequest request,
            @PathVariable("id") UUID plcId
    ) {

        String token = cookiesService.extractJwtFromCookies(request);
        String userEmail = jwtTokenService.getUserEmailFromToken(token);
        User user = userRepository.findByUserEmail(userEmail).orElseThrow();

        return ResponseEntity.ok(dashboardPlcService.getPlcTemperaturesMeasuredOnSensors(plcId, user.getId()));
    }

    /**
     * Fetches a list of continents with the associated count of sensors for the dashboard.
     * <p>
     * This method retrieves user information based on the JWT token in the request cookies,
     * and then uses the user's ID to gather sensor data grouped by continent.
     *
     * @param request the HttpServletRequest containing the client's cookies and other request details
     * @return ResponseEntity wrapping a list of DashboardContinentDto objects, which represent
     * the continents and their associated sensor counts
     */
    @Operation(summary = "Get continents chart", description = "Fetches a list of continents with associated count for Dashboard.")
    @GetMapping(path = "/charts/continents")
    public ResponseEntity<List<DashboardContinentDto>> getContinentsChart(HttpServletRequest request) {
        String token = cookiesService.extractJwtFromCookies(request);
        String userEmail = jwtTokenService.getUserEmailFromToken(token);
        User user = userRepository.findByUserEmail(userEmail).orElseThrow();

        List<DashboardContinentDto> continentsChart = dashboardSensorService.getContinentCountsOnUserSensor(user.getId());
        return ResponseEntity.ok(continentsChart);
    }

    /**
     * Fetches KPIs for the dashboard, including active/inactive sensors
     * and today's temperature extremes.
     *
     * @param request the HttpServletRequest containing the client's
     *                cookies and other request details
     * @return ResponseEntity containing a DashboardKpisDto object
     * that represents the key performance indicators
     */
    @Operation(summary = "Get dashboard KPIs", description = "Fetches KPIs including active/inactive sensors and today's temperature extremes.")
    @GetMapping(path = "/kpis")
    public ResponseEntity<DashboardKpisDto> getDashboardKpis(HttpServletRequest request) {
        String token = cookiesService.extractJwtFromCookies(request);
        String userEmail = jwtTokenService.getUserEmailFromToken(token);
        User user = userRepository.findByUserEmail(userEmail).orElseThrow();

        DashboardKpisDto kpis = dashboardKpiService.getKpis(user.getId());
        return ResponseEntity.ok(kpis);
    }
}