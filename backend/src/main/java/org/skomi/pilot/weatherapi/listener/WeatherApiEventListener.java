package org.skomi.pilot.weatherapi.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.shared.event.GetPlaceWeatherDataEvent;
import org.skomi.pilot.shared.event.GetPlaceWeatherDataHistoryEvent;
import org.skomi.pilot.shared.model.Place;
import org.skomi.pilot.weatherapi.dto.WeatherApiResponseDto;
import org.skomi.pilot.weatherapi.publisher.WeatherApiEventPublisher;
import org.skomi.pilot.weatherapi.service.RestWeatherService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherApiEventListener {

    private final WeatherApiEventPublisher weatherApiEventPublisher;
    private final RestWeatherService restWeatherService;

    /**
     * Handles the `GetPlaceWeatherDataEvent` by fetching weather data for a list of places
     * and publishing an event with updated weather data for those places.
     *
     * @param event the event containing a list of places for which weather data needs to be retrieved
     */
    @EventListener
    private void handleWeatherRequestEvent(GetPlaceWeatherDataEvent event) {
        List<Place> placesWithWeatherData = new ArrayList<>();

        event.getPlaces().forEach(place -> {

            try {
                WeatherApiResponseDto responseDto = restWeatherService.getActualWeather(place.getId());

                place.setCondition(responseDto.getCurrent().getCondition().getText());
                place.setTemperature(responseDto.getCurrent().getTempC());
                place.setContinent(responseDto.getLocation().getTzId().split("/")[0]);
            } catch (Exception e) {

                place.setCondition(null);
                place.setTemperature(null);
                place.setContinent(null);
            }

            placesWithWeatherData.add(place);

            log.info("Received weather data for place {}, {}", place.getId(), place.getTemperature());
        });

        weatherApiEventPublisher.sendWeatherDataEvent(placesWithWeatherData);
    }

    /**
     * Handles the `GetPlaceWeatherDataHistoryEvent` by fetching historical weather data
     * for a specific place and publishing an event with the temperature data for the specified place.
     *
     * @param event the event containing the details of the place for which
     *              historical weather data needs to be retrieved
     */
    @EventListener
    private void handleWeatherHistory(GetPlaceWeatherDataHistoryEvent event) {
        String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<Double> temperatures = restWeatherService.getHistoricalTemperatures(event.getPlace(), yesterday);

        log.info("Received weather history for place {}, {}", event.getPlace(), temperatures);

        weatherApiEventPublisher.sendWeatherDataHistoryEvent(temperatures, event.getPlace());
    }
}
