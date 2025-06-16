package org.skomi.pilot.shared.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Data
public class SensorHistory {

    @Id
    private UUID id;

    private UUID sensorId;

    private Double temperature;

    private String condition;

    private Instant createdAt;

    private Instant updatedAt;
}
