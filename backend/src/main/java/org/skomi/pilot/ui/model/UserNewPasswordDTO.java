package org.skomi.pilot.ui.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Password and email")
public record UserNewPasswordDTO(
        String resetPasswordToken,
        String password
) {
}
