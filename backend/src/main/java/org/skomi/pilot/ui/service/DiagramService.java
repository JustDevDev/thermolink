package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.shared.model.*;
import org.skomi.pilot.shared.repository.UserPlcRepository;
import org.skomi.pilot.shared.service.UserService;
import org.skomi.pilot.ui.model.DiagramDTO;
import org.skomi.pilot.ui.publisher.DiagramEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * Service class for managing diagram data (PLCs and Sensors).
 */
@Service
@RequiredArgsConstructor
public class DiagramService {

    private final UserService userService;
    private final SensorRegistrator sensorRegistrator;
    private final SensorPLCRegistrator sensorPLCRegistrator;
    private final SensorUserRegistrator sensorUserRegistrator;
    private final PlcUserRegistrator plcUserRegistrator;
    private final PlcRegistrator plcRegistrator;
    private final SensorMapper sensorMapper;
    private final PlcMapper plcMapper;
    private final DiagramEventPublisher diagramEventPublisher;
    private final SensorProcessor sensorProcessor;
    private final UserPlcRepository userPlcRepository;

    /**
     * Saves or updates diagram data.
     *
     * @param userEmail the email of the authenticated user
     * @param diagram       the diagram data to save
     * @return saved DiagramDataDto
     */
    public DiagramDTO saveDiagramData(String userEmail, DiagramDTO diagram) {
        // get the user
        User user = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        registerPlcs(diagram, user);

        List<SensorWithData> processedSensors = processSensors(diagram, user);

        sensorPLCRegistrator.registerSensorToPlcConnections(diagram, user);

        // send saved sensors data
        List<SensorWithData> sensorsWithDataToSend = processedSensors
                .stream()
                .filter(sensor -> nonNull(sensor.getTemperature()))
                .toList();

        diagramEventPublisher.sendSensorsToWsck(userEmail, sensorsWithDataToSend);

        return diagram;
    }

    /**
     * Registers a list of sensors from the provided DiagramDTO with the specified user.
     *
     * @param dto  the data transfer object containing information about sensors to be registered
     * @param user the user with whom the sensors will be associated
     * @return a list of sensors with their associated data after registration
     */
    private List<SensorWithData> processSensors(DiagramDTO dto, User user) {
        // save new sensors and update old sensors
        List<Sensor> sensorToSave = sensorMapper.mapToSensors(dto.sensors());
        List<Sensor> registeredSensors = sensorRegistrator.registerSensors(sensorToSave);

        sensorUserRegistrator.registerSensorsToUser(registeredSensors, user);

        // process only sensors with place assigned
        return sensorToSave.stream()
                .filter(sensor -> !sensor.getPlaceId().isBlank())
                .map(sensorProcessor::processSensorPlace).toList();
    }

    /**
     * Registers a list of Programmable Logic Controllers (PLCs) from the provided DiagramDTO
     * and associates them with the specified user.
     *
     * @param dto  the data transfer object containing information about PLCs to be registered
     * @param user the user with whom the PLCs will be associated
     */
    private void registerPlcs(DiagramDTO dto, User user) {
        // Save new PLCs
        List<Plc> plcsToSave = plcMapper.mapToPlcs(dto.PLCs());
        List<Plc> savedPlcs = plcRegistrator.registerPLCs(plcsToSave);
        // Create UserPlc associations
        plcUserRegistrator.registerPLCToUser(savedPlcs, user);

        deletePlcs(user, savedPlcs);
    }

    /**
     * Deletes associations between a user and PLCs that are no longer needed.
     *
     * @param user       the user whose PLC associations are to be updated
     * @param plcsToStay the list of PLCs that should remain associated with the user
     */
    private void deletePlcs(User user, List<Plc> plcsToStay) {
        // Fetch all plc entries for the given user
        List<UserPlc> existingAssociations = userPlcRepository.findAllByUserId(user.getId());

        // Determine associations to remove
        List<UserPlc> plcsToRemove = existingAssociations.stream()
                .filter(userPlc -> plcsToStay.stream()
                        .noneMatch(sensor -> sensor.getId().equals(userPlc.getPlcId())))
                .collect(Collectors.toList());

        // Remove the outdated plc entries
        userPlcRepository.deleteAll(plcsToRemove);
    }
}