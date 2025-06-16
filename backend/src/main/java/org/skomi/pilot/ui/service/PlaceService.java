package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.shared.model.Place;
import org.skomi.pilot.shared.model.PlaceHistory;
import org.skomi.pilot.shared.repository.PlaceRepository;
import org.skomi.pilot.shared.service.PlaceHistoryService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceHistoryService placeHistoryService;

    /**
     * Calculates the average temperature of a given sensor, including the latest recorded temperature
     * and historical temperatures from its associated sensor history records.
     *
     * @param place the sensor for which the average temperature is to be calculated
     * @return the average temperature as a Double, or 0.0 if no temperatures are available
     */
    public Double countPlaceAvg(Place place) {
        List<PlaceHistory> sensors = placeHistoryService.findAllById(place.getId());
        Set<Double> sensorTemps = sensors.stream().map(PlaceHistory::getTemperature).collect(java.util.stream.Collectors.toSet());
        sensorTemps.add(place.getTemperature());

        double average = sensorTemps.stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return BigDecimal.valueOf(average)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }


    /**
     * Saves all the provided Place entities to the repository.
     *
     * @param places the list of Place entities to be saved
     */
    public void saveAll(List<Place> places) {
        placeRepository.saveAll(places);
    }

    public List<Place> findAll() {
        return (List<Place>) placeRepository.findAll();
    }
}