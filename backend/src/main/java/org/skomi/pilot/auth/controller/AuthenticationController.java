package org.skomi.pilot.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.skomi.pilot.auth.dto.AuthMeResponse;
import org.skomi.pilot.auth.dto.LoginRequest;
import org.skomi.pilot.auth.dto.LoginResponse;
import org.skomi.pilot.auth.service.AuthenticationService;
import org.skomi.pilot.auth.service.JwtTokenService;
import org.skomi.pilot.auth.service.TokenBlacklistService;
import org.skomi.pilot.shared.AuthProviders;
import org.skomi.pilot.shared.Messages;
import org.skomi.pilot.shared.exception.UserNotVerifiedException;
import org.skomi.pilot.shared.exception.WrongCredentialsException;
import org.skomi.pilot.shared.model.User;
import org.skomi.pilot.shared.service.CookiesService;
import org.skomi.pilot.shared.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    /**
     * Dependencies
     */
    private final AuthenticationService authenticationService;
    private final TokenBlacklistService blacklistService;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;
    private final CookiesService cookiesService;

    /**
     * Authenticates a user using the provided login credentials.
     * If authentication is successful, returns an {@code AuthResponse} with a generated token.
     * If authentication fails, throws a {@code WrongCredentialsException}.
     *
     * @param loginRequest the login request containing the user's email and password
     * @return a {@code ResponseEntity} containing the authentication response with the generated token
     * @throws WrongCredentialsException if the provided credentials are invalid
     */
    @Operation(summary = "Authenticate a user")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        String token = authenticationService
                .authenticate(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new WrongCredentialsException(Messages.MSG_WRONG_CREDENTIALS));

        Cookie cookie = cookiesService.buildJwtCookie(token);
        response.addCookie(cookie);

        User loggedUser = userService.findUserByEmail(loginRequest.email())
                .orElseThrow(() -> new WrongCredentialsException(Messages.MSG_WRONG_CREDENTIALS));

        if (!loggedUser.getVerified()) throw new UserNotVerifiedException();

        return ResponseEntity.ok().body(new LoginResponse(
                loggedUser.getId(),
                loggedUser.getUserFirstName(),
                loggedUser.getUserLastName(),
                loginRequest.email(),
                loggedUser.getUserAvatar(),
                Messages.MSG_LOGIN_SUCCESS,
                loggedUser.getPassword().equals(AuthProviders.GOOGLE_PROVIDER) ? "google" : "local",
                loggedUser.getHintPassed()
        ));
    }

    /**
     * Logs out a user by invalidating the provided authentication token.
     * The token is extracted from the HTTP request and added to a blacklist to prevent further use.
     *
     * @return a {@code ResponseEntity<Void>} with the appropriate HTTP status code
     */
    @Operation(summary = "Logout a user")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {

        String token = cookiesService.extractJwtFromCookies(request);

        if (token != null) {
            blacklistService.invalidateToken(token);

            Cookie cookie = cookiesService.deleteJwtCookie();
            response.addCookie(cookie);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.badRequest().build();
    }

    /**
     * Retrieves authenticated user information to verify if the session is still valid.
     * The method extracts a JWT token from the HTTP request, validates it,
     * retrieves the associated user's email, and fetches the user details.
     * If the token is invalid or the user cannot be found, a {@code WrongCredentialsException} is thrown.
     *
     * @param request the HTTP request containing the user's authentication token
     * @return a {@code ResponseEntity} containing the authenticated user's information in an {@code AuthMeResponse} object
     * @throws WrongCredentialsException if the authentication token is invalid or the associated user cannot be found
     */
    @Operation(summary = "Get authenticated user info, to check if its still authenticated")
    @GetMapping("/authMe")
    public ResponseEntity<AuthMeResponse> authMe(HttpServletRequest request) {

        String token = cookiesService.extractJwtFromCookies(request);
        if (isNull(token)) throw new WrongCredentialsException("Wrong credentials.");

        String userEmail = jwtTokenService.getUserEmailFromToken(token);
        User user = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new WrongCredentialsException("Wrong credentials."));

        if (!user.getVerified()) throw new UserNotVerifiedException();

        return ResponseEntity.ok(new AuthMeResponse(
                user.getUserEmail(),
                user.getId().toString(),
                user.getUserFirstName(),
                user.getUserLastName(),
                user.getUserAvatar(),
                user.getPassword().equals(AuthProviders.GOOGLE_PROVIDER) ? "google" : "local",
                user.getHintPassed()
                )

        );
    }
}