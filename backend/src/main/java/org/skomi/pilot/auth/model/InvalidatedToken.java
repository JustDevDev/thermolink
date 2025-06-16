package org.skomi.pilot.auth.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@Table("invalidated_token")
@Getter
@Setter
public class InvalidatedToken {
    @Id
    private String tokenHash;

    private OffsetDateTime invalidatedAt;

    private OffsetDateTime expiresAt;
}