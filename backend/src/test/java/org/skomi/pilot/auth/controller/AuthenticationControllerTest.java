package org.skomi.pilot.auth.controller;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skomi.pilot.auth.dto.LoginRequest;
import org.skomi.pilot.auth.dto.LoginResponse;
import org.skomi.pilot.auth.service.AuthenticationService;
import org.skomi.pilot.auth.service.JwtTokenService;
import org.skomi.pilot.auth.service.TokenBlacklistService;
import org.skomi.pilot.shared.Messages;
import org.skomi.pilot.shared.exception.UserNotVerifiedException;
import org.skomi.pilot.shared.exception.WrongCredentialsException;
import org.skomi.pilot.shared.model.User;
import org.skomi.pilot.shared.service.CookiesService;
import org.skomi.pilot.shared.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private TokenBlacklistService blacklistService;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private UserService userService;

    @Mock
    private CookiesService cookiesService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void shouldAuthenticateUserSuccessfully() {
        // given
        String email = "user@example.com";
        String password = "google";
        String token = "mockedJwtToken";
        UUID userId = UUID.randomUUID();
        String userFirstName = "John";
        String userLastName = "Doe";
        String userAvatar = "base64-avatar";
        Boolean hintPassed = true;

        LoginRequest loginRequest = new LoginRequest(email, password);
        MockHttpServletResponse response = new MockHttpServletResponse();

        User user = new User();
        user.setId(userId);
        user.setUserFirstName(userFirstName);
        user.setUserLastName(userLastName);
        user.setUserEmail(email);
        user.setUserAvatar(userAvatar);
        user.setVerified(true);
        user.setHintPassed(hintPassed);
        user.setPassword(password);

        given(authenticationService.authenticate(email, password)).willReturn(Optional.of(token));
        given(cookiesService.buildJwtCookie(token)).willReturn(new Cookie("jwt", token));
        given(userService.findUserByEmail(email)).willReturn(Optional.of(user));

        // when
        ResponseEntity<LoginResponse> responseEntity = authenticationController.login(loginRequest, response);

        // then
        assertThat(response.getCookies()[0].getValue(), is(equalTo(token)));
        assertThat(responseEntity.getStatusCodeValue(), is(200));
        assertThat(responseEntity.getBody().userEmail(), is(email));
        assertThat(responseEntity.getBody().userId(), is(userId));
        assertThat(responseEntity.getBody().message(), is(Messages.MSG_LOGIN_SUCCESS));
        verify(authenticationService).authenticate(email, password);
        verify(cookiesService).buildJwtCookie(token);
        verify(userService).findUserByEmail(email);
    }

    @Test
    void shouldThrowExceptionForInvalidCredentials() {
        // given
        String email = "user@example.com";
        String password = "wrongPassword";
        LoginRequest loginRequest = new LoginRequest(email, password);

        given(authenticationService.authenticate(email, password)).willReturn(Optional.empty());

        // when // then
        org.junit.jupiter.api.Assertions.assertThrows(
                WrongCredentialsException.class,
                () -> authenticationController.login(loginRequest, new MockHttpServletResponse())
        );
        verify(authenticationService).authenticate(email, password);
    }

    @Test
    void shouldThrowExceptionIfUserIsNotVerified() {
        // given
        String email = "user@example.com";
        String password = "password123";
        String token = "mockedJwtToken";

        LoginRequest loginRequest = new LoginRequest(email, password);
        MockHttpServletResponse response = new MockHttpServletResponse();

        User user = new User();
        user.setUserEmail(email);
        user.setVerified(false);

        given(authenticationService.authenticate(email, password)).willReturn(Optional.of(token));
        given(userService.findUserByEmail(email)).willReturn(Optional.of(user));
        given(cookiesService.buildJwtCookie(token)).willReturn(new Cookie("jwt", token));

        // when // then
        org.junit.jupiter.api.Assertions.assertThrows(
                UserNotVerifiedException.class,
                () -> authenticationController.login(loginRequest, response)
        );
        verify(authenticationService).authenticate(email, password);
        verify(userService).findUserByEmail(email);
    }
}