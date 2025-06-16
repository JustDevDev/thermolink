package org.skomi.pilot.ui.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.shared.event.PlaceActualWeatherDataEvent;
import org.skomi.pilot.shared.event.RefreshAllPlacesEvent;
import org.skomi.pilot.shared.event.WsckSessionOpenedEvent;
import org.skomi.pilot.shared.model.Place;
import org.skomi.pilot.ui.publisher.DiagramEventPublisher;
import org.skomi.pilot.ui.service.PlaceService;
import org.skomi.pilot.ui.service.SensorUpdateDistributor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiagramListener {

    private final DiagramEventPublisher diagramEventPublisher;
    private final SensorUpdateDistributor sensorUpdateDistributor;
    private final PlaceService placeService;

    /**
     * Handles the RefreshAllPlacesEvent by retrieving all Place entities from the database
     * and triggering a request for weather data updates for these places through the DiagramEventPublisher.
     *
     * @param event the event indicating that all places should be refreshed and their sensor data updated
     */
    @EventListener
    private void updateSensorData(RefreshAllPlacesEvent event) {
        List<Place> places = placeService.findAll();
        log.info("Updating sensor data.");
        diagramEventPublisher.requestWeatherDataForPlaces(places);
    }

    /**
     * Handles the PlaceActualWeatherDataEvent by updating the average temperature for each Place,
     * saving the updated Place entities, and redistributing the updates to all users who own sensors
     * associated with the given Places.
     *
     * @param event the event containing the list of Place entities whose data needs to be updated
     */
    @EventListener
    private void updatePlaces(PlaceActualWeatherDataEvent event) {
        // set average temp for every sensor
        event.getPlaces().forEach(place -> place.setAverageTemperature(placeService.countPlaceAvg(place)));

        placeService.saveAll(event.getPlaces());

        // send to all current users
        sensorUpdateDistributor.redistributeUpdateToAllUsersOwningSensorsWithPlace(event.getPlaces());
    }

    /**
     * Handles the WsckSessionOpenedEvent, which is triggered when a WebSocket session is opened
     * by a user. This method logs the event and initiates the process of retrieving and sending
     * all sensor data associated with the user.
     *
     * @param event the event containing the email of the user who opened the WebSocket session
     */
    @EventListener
    public void onUserWsckSessionOpened(WsckSessionOpenedEvent event) {
        log.info("User {} will retrieve sensor data on open.", event.getUserEmail());
        sensorUpdateDistributor.findAndSendToUserAllItsSensors(event.getUserEmail());
    }
}
