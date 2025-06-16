package org.skomi.pilot.websocket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.skomi.pilot.websocket.server.WebSocketServer;
import org.skomi.pilot.websocket.service.WebSocketMessageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Configures WebSocket endpoints and services for the application.
 * This class enables WebSocket support and provides necessary beans
 * for handling WebSocket communication.
 */
@EnableWebSocket
@Configuration
public class WebSocketConfig {

    /**
     * Exposes a bean for automatically registering WebSocket endpoints
     * declared with the {@link jakarta.websocket.server.ServerEndpoint} annotation.
     * This bean is enabled only when the property "websocket.enabled" is true or missing.
     *
     * @return an instance of {@link ServerEndpointExporter}.
     */
    @Bean
    @ConditionalOnProperty(name = "websocket.enabled", havingValue = "true", matchIfMissing = true)
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * Configures the {@link WebSocketMessageService} to handle WebSocket messages.
     * This method initializes the message service and registers it with the {@link WebSocketServer}.
     *
     * @param objectMapper the {@link ObjectMapper} used for message serialization and deserialization.
     * @return an instance of {@link WebSocketMessageService}.
     */
    @Bean
    public WebSocketMessageService webSocketMessageService(ObjectMapper objectMapper) {
        WebSocketMessageService service = new WebSocketMessageService(objectMapper);
        WebSocketServer.setMessageService(service);
        return service;
    }
}