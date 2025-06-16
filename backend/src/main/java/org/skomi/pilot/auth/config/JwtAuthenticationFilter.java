package org.skomi.pilot.auth.config;

import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.skomi.pilot.auth.service.JwtTokenService;
import org.skomi.pilot.auth.service.TokenBlacklistService;
import org.skomi.pilot.shared.service.CookiesService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * A filter that processes JWT authentication for incoming requests.
 * The filter extracts a JWT token from cookies, validates it,
 * checks if it is blacklisted, and authenticates the user
 * based on the token's content.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Dependencies
     */
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtTokenService jwtTokenService;
    private final CookiesService cookiesService;

    /**
     * Processes incoming HTTP requests to validate and authenticate JWT tokens.
     *
     * @param request     the HTTP request being processed
     * @param response    the HTTP response to be sent back
     * @param filterChain the filter chain to pass the request/response pair to the next filter
     * @throws ServletException if an error occurs during request processing
     * @throws IOException      if an input or output error is detected when handling the request
     */
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String token = cookiesService.extractJwtFromCookies(request);

        if (token != null) {
            // check if not black-listed
            if (tokenBlacklistService.isTokenInvalidated(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            try {
                String email = jwtTokenService.getUserEmailFromToken(token);
                if (email != null) {
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            email, null, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (SignatureException e) {
                // token validation failed; respond with an unauthorized error and exit filter chain
                log.error("Invalid JWT token: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            }
        }
        filterChain.doFilter(request, response);
    }
}
