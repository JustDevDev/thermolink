package org.skomi.pilot.shared.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GetPlaceWeatherDataHistoryEvent extends ApplicationEvent {

    String place;

    public GetPlaceWeatherDataHistoryEvent(Object source, String place) {
        super(source);
        this.place = place;
    }
}
