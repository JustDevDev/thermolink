package org.skomi.pilot.googleapi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.skomi.pilot.googleapi.model.CityResponse;
import org.skomi.pilot.googleapi.service.CitySearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/place")
@RequiredArgsConstructor
@Tag(name = "City Search", description = "API endpoints for searching cities")
public class CitySearchController {

    private final CitySearchService citySearchService;

    /**
     * Endpoint to search cities by query parameter.
     *
     * @param place the search query for cities.
     * @return a list of cities.
     */
    @GetMapping
    public ResponseEntity<List<CityResponse>> getCities(@RequestParam("place") String place) {
        return ResponseEntity.ok(citySearchService.searchCities(place));
    }
}