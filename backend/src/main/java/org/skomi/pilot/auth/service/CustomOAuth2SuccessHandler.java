package org.skomi.pilot.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.shared.service.CookiesService;
import org.skomi.pilot.shared.service.OAuthUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${app.frontend.url:http://localhost:3001/user/dashboard}")
    private final String frontendUrl;

    private final JwtTokenService jwtTokenService;
    private final CookiesService cookiesService;
    private final OAuthUserService oAuthUserService;

    /**
     * Handles the successful OAuth2 authentication event.
     * This method is triggered when authentication is successfully completed,
     * and it performs additional operations such as generating a JWT token,
     * setting up a secure cookie, saving user details if necessary,
     * and redirecting the user to the frontend dashboard.
     *
     * @param request the HttpServletRequest containing information about the HTTP request
     * @param response the HttpServletResponse for delivering HTTP responses
     * @param authentication the Authentication object containing details of the authenticated user
     * @throws IOException if an input or output exception occurs during the redirect
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        // extract user email from the OAuth2 authentication principal.
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
        String userEmail = oauthUser.getAttribute("email");

        // create a JWT token and build the corresponding cookie.
        String token = jwtTokenService.createToken(userEmail);
        Cookie cookie = cookiesService.buildJwtCookie(token);
        response.addCookie(cookie);

        oAuthUserService.handleOAuthUser(userEmail, oauthUser);

        // redirect the user to the default landing page or a specific page.
        response.sendRedirect(frontendUrl);
    }
}