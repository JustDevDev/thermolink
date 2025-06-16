package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.shared.model.Place;
import org.skomi.pilot.shared.model.Sensor;
import org.skomi.pilot.shared.model.SensorWithData;
import org.skomi.pilot.shared.model.UserSensor;
import org.skomi.pilot.shared.repository.SensorRepository;
import org.skomi.pilot.shared.repository.UserRepository;
import org.skomi.pilot.shared.repository.UserSensorRepository;
import org.skomi.pilot.ui.publisher.DiagramEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Handling distributions of sensor states to users.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SensorUpdateDistributor {

    private final UserSensorRepository userSensorRepository;
    private final DiagramEventPublisher diagramEventPublisher;
    private final UserRepository userRepository;
    private final SensorUserService sensorUserService;
    private final SensorRepository sensorRepository;

    /**
     * Distributes updated sensor data to all users who own any of the provided sensors.
     * This method iterates through the list of sensors and performs the redistribution
     * process for each sensor by invoking the respective method.
     *
     * @param places the list of sensors with updated data that need to be redistributed
     */
    public void redistributeUpdateToAllUsersOwningSensorsWithPlace(List<Place> places) {
        places.forEach(this::redistributeUpdateToAllUsersOwningThisSensorPlace);
    }

    /**
     * Redistributes the updated sensor data to all users associated with the sensor.
     * This method retrieves all user associations for the given sensor, groups the sensors
     * by user email, and publishes the updated sensor data to the respective users.
     *
     * @param place the sensor with updated data that needs to be redistributed
     */
    public void redistributeUpdateToAllUsersOwningThisSensorPlace(Place place) {

        List<Sensor> sensorsOnPlace = sensorRepository.findByPlace(place.getId());
        List<UUID> sensorIds = sensorsOnPlace.stream().map(Sensor::getId).toList();

        List<UserSensor> userSensors = userSensorRepository.findAllBySensorIds(sensorIds);
        Map<UUID, List<SensorWithData>> userSensorsMap = userSensors.stream()
                .collect(Collectors.groupingBy(
                        UserSensor::getUserId,
                        Collectors.mapping(userSensor -> new SensorWithData(userSensor.getSensorId(),
                                place.getId(),
                                place.getTemperature(),
                                place.getAverageTemperature(),
                                place.getCondition()), Collectors.toList())
                ));

        userSensorsMap.keySet().forEach(userEmail ->
                diagramEventPublisher.sendSensorsToWsck(
                        userRepository.findById(userEmail).get().getUserEmail(),
                        userSensorsMap.get(userEmail))
        );
    }


    /**
     * Distributes sensor updates to a specific user by email.
     * This method finds the sensors associated with a user and publishes
     * their updated sensor data.
     *
     * @param userEmail the email of the user to whom the update will be distributed
     */
    public void findAndSendToUserAllItsSensors(String userEmail) {
        userRepository.findByUserEmail(userEmail).ifPresent(
                user -> {
                    List<SensorWithData> associatedSensors = sensorUserService.getSensorsAssociatedToUser(user.getId());
                    if (!associatedSensors.isEmpty()) {
                        diagramEventPublisher.sendSensorsToWsck(userEmail, associatedSensors);
                    }
                }
        );
    }
}