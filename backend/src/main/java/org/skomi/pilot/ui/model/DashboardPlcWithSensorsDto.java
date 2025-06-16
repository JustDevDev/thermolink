package org.skomi.pilot.ui.model;

import java.util.List;
import java.util.UUID;

public record DashboardPlcWithSensorsDto(
        UUID id,
        String name,
        List<DashboardSensorDto> connectedSensors
) {
}