package org.skomi.pilot.websocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.websocket.model.WebSocketContent;
import org.skomi.pilot.websocket.model.WebSocketMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service for managing WebSocket connections and sending messages to users.
 * This service handles maintaining an active user session registry, sending
 * messages to individual users as well as managing session lifecycle events.
 */
@Slf4j
@Service
public class WebSocketMessageService {
    private final ObjectMapper objectMapper;
    private final Map<String, Session> userSessions;
    private final ScheduledExecutorService heartbeatScheduler;
    private static final int HEARTBEAT_INTERVAL_SECONDS = 50;


    public WebSocketMessageService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.userSessions = new ConcurrentHashMap<>();
        this.heartbeatScheduler = Executors.newSingleThreadScheduledExecutor();
        startHeartbeatScheduler();
    }

    /**
     * Starts a scheduler that sends heartbeat messages to all active sessions
     * at regular intervals to prevent connection timeouts.
     */
    private void startHeartbeatScheduler() {
        heartbeatScheduler.scheduleAtFixedRate(
                this::sendHeartbeatToAllSessions,
                HEARTBEAT_INTERVAL_SECONDS,
                HEARTBEAT_INTERVAL_SECONDS,
                TimeUnit.SECONDS
        );
        log.info("WebSocket heartbeat scheduler started with interval of {} seconds", HEARTBEAT_INTERVAL_SECONDS);
    }

    /**
     * Sends a heartbeat message to all active WebSocket sessions.
     */
    private void sendHeartbeatToAllSessions() {
        log.debug("Sending heartbeat to {} active WebSocket sessions", userSessions.size());
        userSessions.forEach((email, session) -> {
            if (session != null && session.isOpen()) {
                WebSocketMessage<WebSocketContent<String>> heartbeat = createWebSocketMessage("heartbeat", "ping");
                sendMessage(session, heartbeat);
            } else {
                log.debug("Removing stale session for user: {}", email);
                userSessions.remove(email);
            }
        });
    }


    /**
     * Registers a WebSocket session for a given user identified by their email.
     *
     * @param email   the email of the user
     * @param session the WebSocket session to be registered
     */
    public void registerSession(String email, Session session) {
        userSessions.put(email, session);
    }

    /**
     * Removes a WebSocket session from the registry for a given user.
     *
     * @param email the email of the user whose session should be removed
     */
    public void removeSession(String email) {
        userSessions.remove(email);
    }

    /**
     * Sends a WebSocket message to a specific user if their session is active.
     *
     * @param email the email of the recipient user
     * @param type  the type of message being sent
     * @param data  the payload data to be included in the message
     * @param <T>   the type of the payload data
     */
    public <T> void sendToUser(String email, String type, T data) {
        Session session = userSessions.get(email);
        if (session != null && session.isOpen()) {
            WebSocketMessage<WebSocketContent<T>> message = createWebSocketMessage(type, data);
            log.info("Sending message to user: {}. {}", email, message);
            sendMessage(session, message);
        } else {
            log.warn("No active session found for user: {}", email);
        }
    }

    /**
     * Creates a WebSocketMessage object encapsulating the specified type and data.
     *
     * @param type the type of the message to be created
     * @param data the content to include in the message
     * @param <T>  the type of the payload data
     * @return a constructed WebSocketMessage with the specified data
     */
    private <T> WebSocketMessage<WebSocketContent<T>> createWebSocketMessage(String type, T data) {
        return new WebSocketMessage<>(
                type,
                new WebSocketContent<>(data)
        );
    }

    /**
     * Sends a serialized JSON message to the specified WebSocket session.
     *
     * @param session the target WebSocket session to send the message to
     * @param message the message object to be serialized and sent
     */
    private void sendMessage(Session session, Object message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            synchronized (session) {
                session.getBasicRemote().sendText(jsonMessage);
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message: {}", e.getMessage());
        } catch (IOException e) {
            log.error("Failed to send message: {}", e.getMessage());
        }
    }
}