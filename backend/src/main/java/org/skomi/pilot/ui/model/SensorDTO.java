package org.skomi.pilot.ui.model;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "Represents a sensor capturing environmental data.")
public record SensorDTO(
        @Schema(description = "Unique identifier of the sensor.")
        UUID id,

        @Schema(description = "Physical place of the sensor.")
        String place,

        @Schema(description = "Connections to the next PLC node. It has id of the PLC node.")
        List<PlcPort> connections
) {
}
