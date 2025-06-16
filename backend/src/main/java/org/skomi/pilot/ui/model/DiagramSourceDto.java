package org.skomi.pilot.ui.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents the source of a diagram, containing diagram data or related information.
 */
public record DiagramSourceDto(
        @Schema(description = "Content of the diagram, represented as a string.")
        String diagram
) {
}
