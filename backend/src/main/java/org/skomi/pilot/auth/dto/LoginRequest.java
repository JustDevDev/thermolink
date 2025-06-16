package org.skomi.pilot.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Represents a request object for user login.
 * For use in {@code AuthenticationController}.
 *
 * @param email    User email
 * @param password User password
 */
public record LoginRequest(
        @NotBlank
        @Email
        @Schema(name = "email",
                description = "The email field is required to be non-blank and must follow a valid email format.",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @NotBlank
        @Schema(name = "password",
                description = "The password field is required to be non-blank.",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {
}