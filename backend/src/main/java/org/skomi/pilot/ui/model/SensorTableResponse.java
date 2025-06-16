package org.skomi.pilot.ui.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.skomi.pilot.shared.model.ConnectedPlcDTO;
import org.skomi.pilot.shared.model.SensorWithData;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorTableResponse extends SensorWithData {

    List<ConnectedPlcDTO> connectedPlcs;
    List<TemperatureRecordDTO> lastTemperatureRecords;

    public SensorTableResponse(SensorWithData sensor, List<ConnectedPlcDTO> connectedPlcs, List<TemperatureRecordDTO> lastTemperatureRecords) {
        this.connectedPlcs = connectedPlcs;
        this.lastTemperatureRecords = lastTemperatureRecords;
        this.setId(sensor.getId());
        this.setCondition(sensor.getCondition());
        this.setPlace(sensor.getPlace());
        this.setTemperature(sensor.getTemperature());
        this.setAverageTemperature(sensor.getAverageTemperature());
    }
}
