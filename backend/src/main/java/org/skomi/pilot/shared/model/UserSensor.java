package org.skomi.pilot.shared.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Schema(description = "Represents a sensor capturing environmental data.")
@Data
@Table(name = "user_sensor")
public class UserSensor {

    @Id
    private Long id;

    private UUID userId;

    private UUID sensorId;
}
