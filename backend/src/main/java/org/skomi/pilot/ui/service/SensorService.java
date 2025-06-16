package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.shared.model.Sensor;
import org.skomi.pilot.shared.repository.SensorHistoryRepository;
import org.skomi.pilot.shared.repository.SensorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Service class for managing {@link Sensor} entities.
 * Provides business logic and transaction management for sensor operations.
 */
@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;
    private final SensorHistoryRepository sensorHistoryRepository;

    /**
     * Saves a sensor entity to the database.
     * If the sensor has an ID, it will update the existing record;
     * otherwise, it will create a new record.
     *
     * @param sensor the sensor entity to save
     */
    public void save(Sensor sensor) {
        sensorRepository.save(sensor);
    }

    /**
     * Saves a list of sensor entities to the database.
     * If a sensor in the list has an ID, it will update the existing record;
     * otherwise, it will create a new record.
     *
     * @param sensor the list of sensor entities to save
     * @return the list of saved sensor entities with generated IDs (if new)
     */
    public List<Sensor> saveAll(List<Sensor> sensor) {
        return sensorRepository.upsertAll(sensor);
    }

    /**
     * Retrieves all sensors from the database.
     *
     * @return a list of all sensors
     */
    public List<Sensor> findAll() {
        return StreamSupport.stream(sensorRepository.findAll().spliterator(),false).toList();
    }

    /**
     * Finds all sensors at a specific place.
     *
     * @param location the place to search for
     * @return a list of sensors at the specified place
     */
    public List<Sensor> findByLocation(String location) {
        return sensorRepository.findByPlace(location);
    }
}