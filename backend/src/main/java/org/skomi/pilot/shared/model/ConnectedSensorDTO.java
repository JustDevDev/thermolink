package org.skomi.pilot.shared.model;

import java.util.UUID;

public record ConnectedSensorDTO(
        UUID id,
        int port,
        String place
) {
    public ConnectedSensorDTO(SensorPlcWithPlace sensorPLC) {
        this(
                sensorPLC.getSensorId(),
                sensorPLC.getPort(),
                sensorPLC.getPlace()
        );
    }
}
