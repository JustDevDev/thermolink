package org.skomi.pilot.shared.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SensorPlc {

    @Id
    private Long id;

    private UUID userId;

    private UUID sensorId;

    private UUID plcId;

    private Integer port;
}