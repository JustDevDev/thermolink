package org.skomi.pilot.ui.model;

import java.util.Date;

public record DashboardTemperatureDto(
        Date date,
        double temperature
) {
}