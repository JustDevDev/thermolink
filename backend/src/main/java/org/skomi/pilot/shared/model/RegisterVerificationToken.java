package org.skomi.pilot.shared.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Table("register_verification_token")
public class RegisterVerificationToken {

    @Id
    private String token;
    private OffsetDateTime created;
    private UUID userId;
}