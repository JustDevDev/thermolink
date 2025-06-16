package org.skomi.pilot.shared.event;

import lombok.Getter;
import org.skomi.pilot.shared.model.Place;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class GetPlaceWeatherDataEvent extends ApplicationEvent {

    List<Place> places;

    public GetPlaceWeatherDataEvent(Object source, List<Place> places) {
        super(source);
        this.places = places;
    }
}
