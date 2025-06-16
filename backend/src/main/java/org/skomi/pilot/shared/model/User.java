package org.skomi.pilot.shared.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("app_user")
@Data
public class User {
    
    @Id
    private UUID id;

    @Schema(description = "Email of the user.", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userEmail;

    @Schema(description = "Stores passwords (hashed)", example = "hashedpassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password; 

    @Schema(description = "First name of the user.", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userFirstName; 

    @Schema(description = "Last name of the user.", example = "Snow", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userLastName; 

    @Schema(description = "User picture in Base64 encoding.")
    private String userAvatar;

    @Schema(description = "If user is verified by email.")
    private Boolean verified;

    @Schema(description = "User avatar in Base64 encoding.")
    private String avatar;

    @Schema(description = "If user passed all hints.")
    private Boolean hintPassed;
}
