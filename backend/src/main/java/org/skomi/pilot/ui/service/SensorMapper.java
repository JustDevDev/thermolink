package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.shared.model.Sensor;
import org.skomi.pilot.ui.model.SensorDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing diagram data (PLCs and Sensors).
 */
@Service
@RequiredArgsConstructor
public class SensorMapper {

    /**
     * Maps a list of SensorDTO to a list of Sensor entities.
     *
     * @param sensorDtos the list of SensorDTOs to map
     * @return a list of mapped Sensor entities
     */
    List<Sensor> mapToSensors(List<SensorDTO> sensorDtos) {
        return sensorDtos.stream()
                .map(dto -> {
                    Sensor sensor = new Sensor();
                    sensor.setId(dto.id() != null ? dto.id() : UUID.randomUUID());
                    sensor.setPlaceId(dto.place());
                    return sensor;
                })
                .collect(Collectors.toList());
    }
}