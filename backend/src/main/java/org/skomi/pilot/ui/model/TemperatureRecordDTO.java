package org.skomi.pilot.ui.model;

import java.time.Instant;

public record TemperatureRecordDTO(
        double temperature,
        Instant date
) {
}
