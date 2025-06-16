package org.skomi.pilot.websocket.config;

import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import org.skomi.pilot.shared.service.CookiesService;

/**
 * Handshake modificator to obtain jwt token from browser cookie
 */
public class CustomConfigurator extends ServerEndpointConfig.Configurator {

    /**
     * Modifies the WebSocket handshake process by extracting a JWT token from the
     * incoming request's cookies and storing it in the user properties map for later access.
     *
     * @param sec      the {@link ServerEndpointConfig} that can be modified to configure the endpoint.
     * @param request  the incoming {@link HandshakeRequest}, containing details such as headers.
     * @param response the outgoing {@link HandshakeResponse}, used to manipulate the response if needed.
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig sec,
                                HandshakeRequest request,
                                HandshakeResponse response) {

        String token = CookiesService.extractJwtFromCookies(request);
        sec.getUserProperties().put(CookiesService.COOKIE_JWT, token);
        super.modifyHandshake(sec, request, response);
    }
}