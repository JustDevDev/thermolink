package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.shared.model.Sensor;
import org.skomi.pilot.shared.repository.SensorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorRegistrator {

    private final SensorRepository sensorRepository;

    /**
     * Registers a list of sensors by either updating existing sensors or creating new ones.
     * For existing sensors, only the sensor data is updated without fetching new data.
     * For new sensors, they are saved and prepared for a weather data request.
     *
     * @param sensorToSave the list of sensors to be registered
     * @return a list of sensors that were successfully registered,
     * including both updated and newly created sensors
     */
    public List<Sensor> registerSensors(List<Sensor> sensorToSave) {
        // save all sensors
        return sensorRepository.upsertAll(sensorToSave);
    }
}