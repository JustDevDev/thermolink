package org.skomi.pilot.shared.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.UUID;

@Schema(description = "Represents a relationship between users and PLCs.")
@Data
@Table("user_plc")
public class UserPlc implements Serializable {

    @Id
    private Long id;

    private UUID userId;

    private UUID plcId;
}
