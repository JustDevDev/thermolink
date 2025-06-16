package org.skomi.pilot.ui.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * Represents a Programmable Logic Controller (PLC), a crucial component in industrial automation.
 */
public record PlcDTO(
        @Schema(description = "Unique identifier for the PLC.")
        UUID id,

        @Schema(description = "Name of the PLC.")
        String name
) {
}
