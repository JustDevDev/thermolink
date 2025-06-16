package org.skomi.pilot.shared.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Error response for providing details about errors encountered during API requests.
 *
 * @param error   A short description of the error type.
 * @param message A detailed message explaining the error.
 */
public record ErrorResponse(

        @Schema(description = "A short description of the error type")
        String error,

        @Schema(description = "A detailed message explaining the error")
        String message
) {
}