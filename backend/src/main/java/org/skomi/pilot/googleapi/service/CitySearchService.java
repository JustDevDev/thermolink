package org.skomi.pilot.googleapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.googleapi.model.CityResponse;
import org.skomi.pilot.googleapi.model.PredictionsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Use of Google autocomplete API for cities.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CitySearchService {


    /**
     * Dependencies
     */
    private final RestTemplate restTemplate;
    @Value("${google.maps.autocomplete.url:https://maps.googleapis.com/maps/api/place/autocomplete/json}")
    private String googleMapsAutocompleteUrl;
    @Value("${google.maps.api.key}")
    private String apiKey;

    /**
     * Searches for cities using Google Places Autocomplete.
     * This method uses the 'types=(cities)' parameter to bias results toward localities.
     *
     * @param input The partial search query.
     * @return A list of up to 10 cities.
     */
    @Cacheable(value = "cities", key = "#input")
    public List<CityResponse> searchCities(String input) {
        String encodedInput = UriUtils.encode(input, StandardCharsets.UTF_8);

        String url = googleMapsAutocompleteUrl +
                     "?input=" +
                     encodedInput +
                     "&types=(cities)" +
                     "&key=" +
                     apiKey;

        log.info(url);

        ResponseEntity<PredictionsResponse> responseEntity =
                restTemplate.getForEntity(url, PredictionsResponse.class);
        AtomicInteger counter = new AtomicInteger(0);

        if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
            return responseEntity.getBody()
                    .getPredictions()
                    .stream()
                    .map(place -> new CityResponse(
                            place.getStructuredFormatting().getMainText(),
                            String.valueOf(counter.getAndIncrement())
                    )).toList();
        }

        log.warn("No response from Google Maps Autocomplete API.");
        return Collections.emptyList();
    }
}