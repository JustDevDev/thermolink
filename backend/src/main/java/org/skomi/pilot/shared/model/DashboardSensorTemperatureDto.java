package org.skomi.pilot.shared.model;

import java.util.Date;
import java.util.UUID;

public record DashboardSensorTemperatureDto(
        UUID sensorId,
        Date date,
        Double temperature
) {
}