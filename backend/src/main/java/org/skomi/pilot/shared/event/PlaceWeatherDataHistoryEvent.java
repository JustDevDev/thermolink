package org.skomi.pilot.shared.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class PlaceWeatherDataHistoryEvent extends ApplicationEvent {

    String place;
    List<Double> temperatures;

    public PlaceWeatherDataHistoryEvent(Object source, String place, List<Double> temperatures) {
        super(source);
        this.place = place;
        this.temperatures = temperatures;
    }
}
