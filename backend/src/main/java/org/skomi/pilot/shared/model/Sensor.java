package org.skomi.pilot.shared.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Schema(description = "Represents a sensor capturing environmental data.")
@Table("sensor")
@Data
@ToString
public class Sensor {

    @Id
    @Schema(description = "Unique identifier of the sensor.")
    private UUID id;

    @Schema(description = "Physical place of the sensor.")
    private String placeId;

    @Schema(description = "Name of the sensor.")
    private String name;
}
