package org.skomi.pilot.shared.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class WsckSessionOpenedEvent extends ApplicationEvent {

    String userEmail;

    public WsckSessionOpenedEvent(Object source, String userEmail) {
        super(source);
        this.userEmail = userEmail;
    }
}
