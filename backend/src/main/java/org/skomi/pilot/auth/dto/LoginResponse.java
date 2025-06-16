package org.skomi.pilot.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * Represents the response returned upon successful user login.
 * Contains user-related details and authentication token.
 *
 * @param userFirstName The first name of the authenticated user.
 * @param userLastName  The last name of the authenticated user.
 * @param userEmail     The email of the authenticated user.
 * @param userAvatar    The BASE64-encoded avatar of the authenticated user.
 * @param message       A message code providing supplementary information about the login result.
 */
public record LoginResponse(
        @Schema(description = "User id")
        UUID userId,

        @Schema(description = "User first name")
        String userFirstName,

        @Schema(description = "User last name")
        String userLastName,

        @Schema(description = "User email")
        String userEmail,

        @Schema(description = "BASE64 user avatar.")
        String userAvatar,

        @Schema(description = "Message code", example = "cannot-log-in")
        String message,

        @Schema(description = "Authentication authProvider", example = "google")
        String authProvider,

        @Schema(description = "If user passed all hints.")
        Boolean hintPassed
) {
}