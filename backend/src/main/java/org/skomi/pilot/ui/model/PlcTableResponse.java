package org.skomi.pilot.ui.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.skomi.pilot.shared.model.ConnectedSensorDTO;
import org.skomi.pilot.shared.model.Plc;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlcTableResponse extends Plc {

    List<ConnectedSensorDTO> connectedSensors;

    public PlcTableResponse(Plc plc, List<ConnectedSensorDTO> connectedSensors) {
        this.setId(plc.getId());
        this.setName(plc.getName());
        this.connectedSensors = connectedSensors;
    }
}
