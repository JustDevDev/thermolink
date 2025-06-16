package org.skomi.pilot.ui.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Represents a Diagram entity that holds information about sensors and PLCs.
 */
@Schema(description = "Represents a Diagram entity containing a list of sensors and PLCs.")
public record DiagramDTO(
        @Schema(description = "List of sensors associated with the diagram.")
        List<SensorDTO> sensors,

        @Schema(description = "List of PLCs (Programmable Logic Controllers) associated with the diagram.")
        List<PlcDTO> PLCs
) {
}
