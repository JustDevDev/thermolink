package org.skomi.pilot.shared.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SensorPlcWithPlace extends SensorPlc {

    String place;
}