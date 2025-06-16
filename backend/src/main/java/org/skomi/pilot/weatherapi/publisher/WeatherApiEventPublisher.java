package org.skomi.pilot.weatherapi.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.shared.event.PlaceActualWeatherDataEvent;
import org.skomi.pilot.shared.event.PlaceWeatherDataHistoryEvent;
import org.skomi.pilot.shared.model.Place;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherApiEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Publishes an event containing the weather data of the specified places.
     * The event is created using the provided list of `Place` objects and
     * dispatched using the `ApplicationEventPublisher`.
     *
     * @param sensors the list of places for which the weather data is included in the event
     */
    public void sendWeatherDataEvent(List<Place> sensors) {
        PlaceActualWeatherDataEvent event = new PlaceActualWeatherDataEvent(this, sensors);
        applicationEventPublisher.publishEvent(event);
    }

    /**
     * Publishes an event containing the historical weather data for a specific place.
     * The event includes a list of temperature values and the name of the place.
     *
     * @param temperatures the list of temperature values representing the historical weather data
     * @param place the name of the place for which the historical weather data is being published
     */
    public void sendWeatherDataHistoryEvent(List<Double> temperatures, String place) {
        PlaceWeatherDataHistoryEvent event = new PlaceWeatherDataHistoryEvent(this, place, temperatures);
        applicationEventPublisher.publishEvent(event);
    }
}
