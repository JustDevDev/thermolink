package org.skomi.pilot.auth.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skomi.pilot.shared.model.User;
import org.skomi.pilot.shared.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void shouldAuthenticateUserWhenCredentialsAreValid() {
        // given
        String email = "user@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";
        String jwtToken = "jwtToken";
        User user = new User();
        user.setUserEmail(email);
        user.setPassword(encodedPassword);

        given(userRepository.findByUserEmail(email)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(password, encodedPassword)).willReturn(true);
        given(jwtTokenService.createToken(email)).willReturn(jwtToken);

        // when
        Optional<String> result = authenticationService.authenticate(email, password);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(jwtToken));
        verify(userRepository).findByUserEmail(email);
        verify(passwordEncoder).matches(password, encodedPassword);
        verify(jwtTokenService).createToken(email);
    }

    @Test
    void shouldNotAuthenticateUserWhenEmailDoesNotExist() {
        // given
        String email = "user@example.com";
        String password = "password";

        given(userRepository.findByUserEmail(email)).willReturn(Optional.empty());

        // when
        Optional<String> result = authenticationService.authenticate(email, password);

        // then
        assertThat(result.isPresent(), is(false));
        verify(userRepository).findByUserEmail(email);
    }

    @Test
    void shouldNotAuthenticateUserWhenPasswordIsInvalid() {
        // given
        String email = "user@example.com";
        String invalidPassword = "invalidPassword";
        String encodedPassword = "encodedPassword";
        User user = new User();
        user.setUserEmail(email);
        user.setPassword(encodedPassword);

        given(userRepository.findByUserEmail(email)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(invalidPassword, encodedPassword)).willReturn(false);

        // when
        Optional<String> result = authenticationService.authenticate(email, invalidPassword);

        // then
        assertThat(result.isPresent(), is(false));
        verify(userRepository).findByUserEmail(email);
        verify(passwordEncoder).matches(invalidPassword, encodedPassword);
    }
}