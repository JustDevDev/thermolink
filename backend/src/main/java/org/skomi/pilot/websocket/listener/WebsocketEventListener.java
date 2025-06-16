package org.skomi.pilot.websocket.listener;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.shared.event.WsckSensorUpdateDataEvent;
import org.skomi.pilot.websocket.service.WebSocketMessageService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Listens for events
 */
@Service
@RequiredArgsConstructor
public class WebsocketEventListener {

    private final WebSocketMessageService webSocketMessageService;

    /**
     * Handles the `WsckSensorUpdateDataEvent` by sending sensor data through a WebSocket
     * to a specific user.
     *
     * @param event the event containing the user identifier and the list of sensors to be sent
     */
    @EventListener
    public void handleUpdateDataEvent(WsckSensorUpdateDataEvent event) {
        webSocketMessageService.sendToUser(event.getUser(),
                WsckSensorUpdateDataEvent.MESSAGE_TYPE,
                event.getSensors());
    }
}
