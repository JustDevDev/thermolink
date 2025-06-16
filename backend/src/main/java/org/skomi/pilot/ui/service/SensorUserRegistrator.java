package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.shared.model.Sensor;
import org.skomi.pilot.shared.model.User;
import org.skomi.pilot.shared.model.UserSensor;
import org.skomi.pilot.shared.repository.UserSensorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Registering sensor with plc connections
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SensorUserRegistrator {

    private final UserSensorRepository userSensorRepository;

    /**
     * Registers a list of sensors to a specific user by creating and persisting new associations,
     * and removing obsolete associations.
     *
     * @param sensors the list of sensors to register to the user
     * @param user    the user to whom the sensors will be associated
     */
    public void registerSensorsToUser(List<Sensor> sensors, User user) {
        // Create Sensor with user associations
        persistSensorWithUser(sensors, user);

        // Remove obsolete sensor associations
        deleteAllOldSensorsOnUser(sensors, user);
    }


    /**
     * Creates associations between a list of saved sensors and a user, then persists the associations
     * in the database.
     *
     * @param savedSensors the list of sensors to associate with the user
     * @param user         the user to associate the sensors with
     */
    private void persistSensorWithUser(List<Sensor> savedSensors, User user) {
        // Create UserSensor associations
        List<UserSensor> userSensorAssociations = savedSensors.stream()
                .map(sensor -> {
                    UserSensor userSensor = new UserSensor();
                    userSensor.setUserId(user.getId());
                    userSensor.setSensorId(sensor.getId());

                    return userSensor;
                })
                .collect(Collectors.toList());

        userSensorRepository.upsertAll(userSensorAssociations);
    }

    /**
     * Removes any old sensor associations that are no longer relevant to the given user.
     *
     * @param sensors the list of current sensors
     * @param user    the user to remove old sensor associations for
     */
    private void deleteAllOldSensorsOnUser(List<Sensor> sensors, User user) {
        // Fetch all UserSensor entries for the given user
        List<UserSensor> existingAssociations = userSensorRepository.findAllByUserId(user.getId());

        // Determine associations to remove
        List<UserSensor> sensorsToRemove = existingAssociations.stream()
                .filter(userSensor -> sensors.stream()
                        .noneMatch(sensor -> sensor.getId().equals(userSensor.getSensorId())))
                .collect(Collectors.toList());

        // Remove the outdated UserSensor entries
        userSensorRepository.deleteAll(sensorsToRemove);
    }
}