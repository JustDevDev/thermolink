package org.skomi.pilot.websocket.publisher;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.shared.event.WsckSessionOpenedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebsocketEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Publishes a WsckSessionOpenedEvent to notify that a new WebSocket session
     * has been opened for a specific user.
     *
     * @param userEmail the email address of the user for whom the WebSocket session
     *                  has been opened. Must not be null or empty.
     */
    public void notifySessionOpened(String userEmail) {
        WsckSessionOpenedEvent event = new WsckSessionOpenedEvent(this, userEmail);
        applicationEventPublisher.publishEvent(event);
    }
}
