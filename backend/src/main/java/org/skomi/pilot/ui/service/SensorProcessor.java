package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.shared.model.Place;
import org.skomi.pilot.shared.model.Sensor;
import org.skomi.pilot.shared.model.SensorWithData;
import org.skomi.pilot.shared.repository.PlaceRepository;
import org.skomi.pilot.ui.publisher.DiagramEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorProcessor {

    private final PlaceService placeService;
    private final PlaceRepository placeRepository;
    private final DiagramEventPublisher diagramEventPublisher;

    /**
     * Processes the given sensor by retrieving or updating data for its associated place.
     * If the place data exists, it populates the returned SensorWithData object with the current conditions.
     * Otherwise, it creates a new place record and requests updated data for the place.
     *
     * @param sensor the sensor whose associated data and place are to be processed
     * @return a SensorWithData object containing the sensor's ID, place, temperature, condition, and average temperature
     */
    public SensorWithData processSensorPlace(Sensor sensor) {

        SensorWithData sensorWithData = new SensorWithData();
        sensorWithData.setId(sensor.getId());
        sensorWithData.setPlace(sensor.getPlaceId());

        // find if place exists
        placeRepository.findById(sensor.getPlaceId()).ifPresentOrElse(
                place -> {
                    sensorWithData.setTemperature(place.getTemperature());
                    sensorWithData.setCondition(place.getCondition());
                    sensorWithData.setAverageTemperature(placeService.countPlaceAvg(place));
                },
                // else create new place
                // new created sensors are not included in immediate response
                // new created sensors will be sent to user when data is ready
                () -> {
                    placeRepository.upsert(sensor.getPlaceId());

                    // need to fill last 10 temperatures from history
                    callForNewPlaceHistoryData(sensor.getPlaceId());

                    // set as waiting for new data it will be sent when received
                    callForNewPlaceData(sensor);
                }
        );

        // returns sensors with data
        return sensorWithData;
    }

    /**
     * Requests new weather data for the specified sensor's associated place.
     *
     * @param sensor the sensor whose associated place requires new weather data
     */
    private void callForNewPlaceData(Sensor sensor) {
        Place place = new Place(sensor.getPlaceId());

        log.info("Call for places new weather data: {}.", place);
        diagramEventPublisher.requestWeatherDataForPlaces(List.of(place));
    }

    /**
     * Requests historical weather data for a specified place.
     *
     * @param placeId the unique identifier of the place for which historical weather data is requested
     */
    private void callForNewPlaceHistoryData(String placeId) {

        log.info("Call for places new weather history data: {}.", placeId);
        diagramEventPublisher.requestWeatherDataHistoryForPlace(placeId);
    }
}
