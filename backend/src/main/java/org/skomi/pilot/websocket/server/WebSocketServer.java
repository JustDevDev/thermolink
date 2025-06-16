package org.skomi.pilot.websocket.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.auth.service.JwtTokenService;
import org.skomi.pilot.auth.service.TokenBlacklistService;
import org.skomi.pilot.websocket.config.CustomConfigurator;
import org.skomi.pilot.websocket.publisher.WebsocketEventPublisher;
import org.skomi.pilot.websocket.service.WebSocketMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ServerEndpoint(value = "/ws/diagram", configurator = CustomConfigurator.class)
public class WebSocketServer {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int AUTH_TIMEOUT_SECONDS = 30;
    private static final String ERROR_TYPE = "error";
    private static final String ERROR_MESSAGE_AUTHENTICATION_FAILED = "Authentication failed";
    private static final String ERROR_MESSAGE_AUTH_TIMEOUT = "Authentication timeout";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials";
    private static JwtTokenService jwtTokenService;
    private static TokenBlacklistService tokenBlacklistService;
    private static WebSocketMessageService messageService;
    private static WebsocketEventPublisher websocketEventPublisher;

    public static void setMessageService(WebSocketMessageService service) {
        WebSocketServer.messageService = service;
    }

    @Autowired
    public void setJwtTokenService(JwtTokenService service) {
        WebSocketServer.jwtTokenService = service;
    }

    @Autowired
    public void setTokenBlacklistService(TokenBlacklistService service) {
        WebSocketServer.tokenBlacklistService = service;
    }

    @Autowired
    public void setEventPublisher(WebsocketEventPublisher publisher) {
        WebSocketServer.websocketEventPublisher = publisher;
    }

    /**
     * Invoked when a new WebSocket connection is established.
     *
     * @param session the newly opened WebSocket session
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("New WebSocket connection established. Session ID: {}", session.getId());
        String token = (String) session.getUserProperties().get("jwt");
        log.info("WebSocket connection opened with token: {}", token);
        if (token == null) {
            log.warn("No token found during handshake. Consider handling authentication failure.");
            scheduleAuthenticationTimeout(session);
        } else {
            authenticateSession(session, token);
        }
    }

    /**
     * Handles incoming WebSocket messages.
     *
     * @param session the session sending the message
     * @param message the message content
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        log.info("Received WebSocket message from session {}: {}", session.getId(), message);
        String email = getEmailFromSession(session);
        if (email != null) {
            websocketEventPublisher.notifySessionOpened(email);
        }
    }

    /**
     * Closes and cleans up when a session ends.
     *
     * @param session the session that is closing
     */
    @OnClose
    public void onClose(Session session) {
        String email = getEmailFromSession(session);
        if (email != null) {
            messageService.removeSession(email);
            log.info("User disconnected: {}", email);
        }
        log.info("WebSocket connection closed. Session ID: {}", session.getId());
    }

    /**
     * Handles errors during session processing.
     *
     * @param session   the session where error occurred
     * @param throwable the error encountered
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        String email = getEmailFromSession(session);
        if (email != null) {
            messageService.removeSession(email);
        }
        log.error("Error in WebSocket session {}: {}", session.getId(), throwable.getMessage(), throwable);
    }

    /**
     * Authenticates the WebSocket session based on the provided token.
     *
     * @param session the WebSocket session to authenticate
     * @param token   the JWT token provided during the session handshake
     */
    private void authenticateSession(Session session, String token) {
        try {
            String userEmail = jwtTokenService.getUserEmailFromToken(token);
            if (isTokenValid(token, userEmail)) {
                handleValidAuthentication(userEmail, session);
            } else {
                handleInvalidAuthentication(userEmail, session);
            }
        } catch (Exception e) {
            log.error("Authentication error: {}", e.getMessage());
            sendErrorMessage(session, ERROR_MESSAGE_AUTHENTICATION_FAILED);
        }
    }

    /**
     * Registers the session for a valid token.
     *
     * @param userEmail the authenticated user's email
     * @param session   the WebSocket session to register
     */
    private void handleValidAuthentication(String userEmail, Session session) {
        messageService.registerSession(userEmail, session);
        session.getUserProperties().put("email", userEmail);
        log.info("User authenticated: {}", userEmail);
    }

    /**
     * Handles the session when token authentication fails.
     *
     * @param userEmail the user's email derived from the token
     * @param session   the WebSocket session to close
     * @throws IOException if an error occurs while sending a message or closing the session
     */
    private void handleInvalidAuthentication(String userEmail, Session session) throws IOException {
        messageService.sendToUser(userEmail, ERROR_TYPE, Map.of("message", INVALID_CREDENTIALS_MESSAGE));
        session.close();
    }

    /**
     * Retrieves the authenticated email from the session's user properties.
     *
     * @param session the WebSocket session
     * @return the email stored in session properties or null if not available
     */
    private String getEmailFromSession(Session session) {
        return (String) session.getUserProperties().get("email");
    }

    /**
     * Sends an error message to the client via the session.
     *
     * @param session      the WebSocket session to send the message on
     * @param errorMessage the error message content
     */
    private void sendErrorMessage(Session session, String errorMessage) {
        try {
            session.getBasicRemote().sendText(objectMapper.writeValueAsString(
                    Map.of("type", ERROR_TYPE, "message", errorMessage)
            ));
        } catch (IOException e) {
            log.error("Error sending error message: {}", e.getMessage());
        }
    }

    /**
     * Validates the token by checking if it is blacklisted, expired, and if the email matches.
     *
     * @param token the JWT token to validate
     * @param email the expected user email extracted from the token
     * @return true if the token is valid; false otherwise
     */
    private boolean isTokenValid(String token, String email) {
        try {
            if (tokenBlacklistService.isTokenInvalidated(token)) {
                log.debug("Token is blacklisted");
                return false;
            }
            Date expiration = jwtTokenService.getExpirationDateFromToken(token);
            if (expiration.before(new Date())) {
                log.debug("Token is expired");
                return false;
            }
            String tokenEmail = jwtTokenService.getUserEmailFromToken(token);
            return email != null && email.equals(tokenEmail);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Schedules a timeout for authentication. If the session is not authenticated
     * within the specified time, an error message is sent and the session is closed.
     *
     * @param session the WebSocket session to monitor
     */
    private void scheduleAuthenticationTimeout(Session session) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> closeSessionIfUnauthenticated(session), AUTH_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * Closes the session if it remains unauthenticated and sends an authentication timeout message.
     *
     * @param session the WebSocket session to check and close if necessary
     */
    private void closeSessionIfUnauthenticated(Session session) {
        try {
            if (!session.getUserProperties().containsKey("email")) {
                sendErrorMessage(session, ERROR_MESSAGE_AUTH_TIMEOUT);
                session.close();
            }
        } catch (IOException e) {
            log.error("Error closing timed-out session: {}", e.getMessage());
        }
    }
}