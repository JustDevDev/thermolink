package org.skomi.pilot.shared.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Schema(description = "Represents a sensor capturing environmental data.")
@Table("sensor")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SensorWithData {

    @Id
    @Schema(description = "Unique identifier of the sensor.")
    private UUID id;

    @Schema(description = "Physical place of the sensor.")
    private String place;

    @Schema(description = "Temperature reading from the sensor.")
    private Double temperature;

    @Schema(description = "Average Day Temperature reading from the sensor.")
    private Double averageTemperature;

    @Schema(description = "Description of the environmental condition.")
    private String condition;
}
