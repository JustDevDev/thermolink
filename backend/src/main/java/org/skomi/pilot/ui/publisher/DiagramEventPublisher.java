package org.skomi.pilot.ui.publisher;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.shared.event.GetPlaceWeatherDataEvent;
import org.skomi.pilot.shared.event.GetPlaceWeatherDataHistoryEvent;
import org.skomi.pilot.shared.event.WsckSensorUpdateDataEvent;
import org.skomi.pilot.shared.model.Place;
import org.skomi.pilot.shared.model.SensorWithData;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagramEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Publishes an event to request weather data for the provided list of sensors.
     *
     * @param places the list of sensors for which weather data is to be requested
     */
    public void requestWeatherDataForPlaces(List<Place> places) {
        GetPlaceWeatherDataEvent event = new GetPlaceWeatherDataEvent(this, places);
        applicationEventPublisher.publishEvent(event);
    }

    /**
     * Publishes an event to request historical weather data for a specific place.
     *
     * @param placeId the unique identifier of the place for which historical weather data is to be requested
     */
    public void requestWeatherDataHistoryForPlace(String placeId) {
        GetPlaceWeatherDataHistoryEvent event = new GetPlaceWeatherDataHistoryEvent(this, placeId);
        applicationEventPublisher.publishEvent(event);
    }

    /**
     * Publishes an event to notify WebSocket listeners about the updated sensor data for a specific user.
     *
     * @param user    the identifier of the user associated with the updated sensors
     * @param sensors the list of sensors with the updated data to be sent
     */
    public void sendSensorsToWsck(String user, List<SensorWithData> sensors) {
        WsckSensorUpdateDataEvent event = new WsckSensorUpdateDataEvent(
                this,
                user,
                sensors
        );
        applicationEventPublisher.publishEvent(event);
    }
}
