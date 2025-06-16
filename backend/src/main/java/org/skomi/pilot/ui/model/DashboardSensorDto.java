package org.skomi.pilot.ui.model;

import java.util.List;
import java.util.UUID;

public record DashboardSensorDto(
        UUID id,
        String place,
        int port,
        List<DashboardTemperatureDto> temperatures
) {
}