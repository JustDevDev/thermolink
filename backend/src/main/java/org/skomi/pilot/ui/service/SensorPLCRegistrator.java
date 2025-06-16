package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.shared.model.SensorPlc;
import org.skomi.pilot.shared.model.User;
import org.skomi.pilot.shared.repository.SensorPLCRepository;
import org.skomi.pilot.ui.model.DiagramDTO;
import org.skomi.pilot.ui.model.PlcDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Registering sensor with plc connections
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SensorPLCRegistrator {

    private final SensorPLCRepository sensorPLCRepository;

    public void registerSensorToPlcConnections(DiagramDTO dto, User user) {
        // Create plc - sensor associations
        List<SensorPlc> sensorPlcConnections = dto.sensors()
                .stream()
                .flatMap(sensorDto -> sensorDto.connections()
                        .stream()
                        .map(connection -> {

                            SensorPlc sensorPlc = new SensorPlc();
                            sensorPlc.setSensorId(sensorDto.id());

                            Optional<PlcDTO> plcDto = dto.PLCs()
                                    .stream()
                                    .filter(plc -> plc.id()
                                            .equals(UUID.fromString(connection.getPLCId())))
                                    .findFirst();

                            if (plcDto.isEmpty()) {
                                return null;
                            }

                            sensorPlc.setUserId(user.getId());
                            sensorPlc.setPlcId(plcDto.get().id());
                            sensorPlc.setPort(connection.getPort());

                            return sensorPlc;
                        })
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        sensorPLCRepository.upsertAll(sensorPlcConnections);
        deleteUnusedConnections(sensorPlcConnections, user);
    }

    /**
     * Deletes unused connections for the specified user by removing associations
     * from the database that do not match the given list of connections to retain.
     *
     * @param sensorPlcConnectionsToStay the list of SensorPlc connections that should be retained
     * @param user                       the User whose connections are being managed
     */
    public void deleteUnusedConnections(List<SensorPlc> sensorPlcConnectionsToStay, User user) {
        // Fetch all UserSensor entries for the given user
        List<SensorPlc> existingAssociations = sensorPLCRepository.findSensorPlcsByUserId(user.getId());

        // Determine associations to remove
        List<SensorPlc> connectionsToRemove = existingAssociations.stream()
                .filter(sensorPlc -> sensorPlcConnectionsToStay.stream()
                        .noneMatch(connection ->
                                connection.getSensorId().equals(sensorPlc.getSensorId())
                                        && connection.getPlcId().equals(sensorPlc.getPlcId())
                                        && connection.getPort().equals(sensorPlc.getPort())
                        )
                )
                .toList();

        // Remove the outdated UserSensor entries
        sensorPLCRepository.deleteAll(connectionsToRemove);
    }
}