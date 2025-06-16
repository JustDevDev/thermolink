package org.skomi.pilot.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Authentication check response.
 *
 * @param userEmail Email of the authenticated user.
 * @param userId    Id of the user
 */
public record AuthMeResponse(

        @Schema(description = "Email of the authenticated user")
        String userEmail,

        @Schema(description = "User id")
        String userId,

        @Schema(description = "User first name")
        String userFirstName,

        @Schema(description = "User last name")
        String userLastName,

        @Schema(description = "BASE64 user avatar.")
        String userAvatar,

        @Schema(description = "Authentication authProvider", example = "google")
        String authProvider,

        @Schema(description = "If user passed all hints.")
        Boolean hintPassed
) {
}