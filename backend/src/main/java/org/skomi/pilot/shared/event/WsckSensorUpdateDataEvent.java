package org.skomi.pilot.shared.event;

import lombok.Getter;
import org.skomi.pilot.shared.model.SensorWithData;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class WsckSensorUpdateDataEvent extends ApplicationEvent {

    public static final String MESSAGE_TYPE = "diagram";
    List<SensorWithData> sensors;
    String user;

    public WsckSensorUpdateDataEvent(Object source, String user, List<SensorWithData> sensors) {
        super(source);
        this.sensors = sensors;
        this.user = user;
    }
}
