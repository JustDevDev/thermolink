package org.skomi.pilot.shared.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RefreshAllPlacesEvent extends ApplicationEvent {

    public RefreshAllPlacesEvent(Object source) {
        super(source);
    }
}
