package org.skomi.pilot.ui.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object for user profile update requests.
 * Contains fields for updating user's personal information and password.
 */
@Schema(description = "Request object for updating user profile information")
public record UserUpdateRequestDTO(
        @Schema(description = "User's first name", example = "John")
        String userFirstName,

        @Schema(description = "User's last name", example = "Doe")
        String userLastName,

        @Schema(description = "URL or Base64 encoded string of user's avatar image")
        String userAvatar,

        @Schema(description = "Current password for verification")
        String currentPassword,

        @Schema(description = "New password to set (optional)")
        String newPassword,

        @Schema(description = "If user passed all hints.")
        Boolean hintPassed
) {
}
