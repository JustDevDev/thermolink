package org.skomi.pilot.weatherapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.weatherapi.dto.HistoricalWeatherResponseDto;
import org.skomi.pilot.weatherapi.dto.WeatherApiResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestWeatherService {

    private final RestTemplateService restTemplateService;

    @Value("${weatherapi.key:noKey}")
    private String apiKey;

    @Value("${weatherapi.base-url:https://api.weatherapi.com/v1}")
    private String baseUrl;

    /**
     * Fetches the current weather information for the specified city.
     *
     * @param city The name of the city for which the current weather data is to be fetched.
     * @return A WeatherApiResponseDto object containing the place details and current weather conditions.
     */
    public WeatherApiResponseDto getActualWeather(String city) {
        String url = baseUrl + "/current.json";

        // Set up query parameters
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("q", stripAccents(city));
        queryParams.put("key", apiKey);

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        return restTemplateService.get(
                url,
                WeatherApiResponseDto.class,
                headers,
                queryParams
        );
    }

    /**
     * Removes any diacritical marks (accents) from the input string, normalizing it.
     *
     * @param value the input string from which accents are to be stripped; can be null
     * @return a normalized string with accents removed, or an empty string if the input is null
     */
    private String stripAccents(String value) {
        return Optional.ofNullable(value)
                .map(v -> Normalizer.normalize(v, Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", ""))
                .orElse("");
    }


    /**
     * Fetches historical weather data for a given city on a specified date,
     * and returns the temperatures for the first 10 hours from the forecast.
     *
     * @param city the city name (e.g., Prague)
     * @param date the date in yyyy-MM-dd format (e.g., 2025-04-17)
     * @return a list of Double values representing the first 10 hourly temperatures
     */
    public List<Double> getHistoricalTemperatures(String city, String date) {
        String url = baseUrl + "/history.json";

        // Set up query parameters
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("q", stripAccents(city));
        queryParams.put("dt", date);
        queryParams.put("key", apiKey);

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        // Call the history API endpoint
        HistoricalWeatherResponseDto response = null;
        try {
            response = restTemplateService.get(
                    url,
                    HistoricalWeatherResponseDto.class,
                    headers,
                    queryParams
            );
        } catch (Exception e) {
            log.error("Error fetching historical weather data for {} on {}. Ignoring.", city, date);
        }

        List<Double> temperatures = new ArrayList<>();
        if (response != null &&
            response.getForecast() != null &&
            response.getForecast().getForecastday() != null &&
            !response.getForecast().getForecastday().isEmpty()) {

            List<HistoricalWeatherResponseDto.Hour> hours = response.getForecast().getForecastday().getFirst().getHour();
            for (int i = 0; i < Math.min(10, hours.size()); i++) {
                temperatures.add(hours.get(i).getTemp_c());
            }
        }
        return temperatures;
    }

}
