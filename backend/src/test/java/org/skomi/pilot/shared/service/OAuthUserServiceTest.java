package org.skomi.pilot.shared.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skomi.pilot.shared.AuthProviders;
import org.skomi.pilot.shared.model.User;
import org.skomi.pilot.shared.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class OAuthUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ImageProcessor imageProcessor;

    @InjectMocks
    private OAuthUserService oAuthUserService;

    @Test
    void shouldCreateNewOAuthUserWhenUserDoesNotExist() throws Exception {
        // given
        String userEmail = "test@example.com";
        DefaultOAuth2User oauthUser = new DefaultOAuth2User(
                null,
                Map.of("name", "Test", "family_name", "User", "picture", "http://example.com/avatar.jpg"),
                "name"
        );

        given(userRepository.findByUserEmail(userEmail)).willReturn(Optional.empty());
        given(imageProcessor.getAvatarFromUrl("http://example.com/avatar.jpg")).willReturn("base64avatar");

        // when
        oAuthUserService.handleOAuthUser(userEmail, oauthUser);

        // then
        then(userRepository).should().save(argThat(user -> {
            assertThat(user.getUserEmail(), is(userEmail));
            assertThat(user.getUserFirstName(), is("Test"));
            assertThat(user.getUserLastName(), is("User"));
            assertThat(user.getPassword(), is(AuthProviders.GOOGLE_PROVIDER));
            assertThat(user.getUserAvatar(), is("base64avatar"));
            assertThat(user.getVerified(), is(true));
            return true;
        }));
    }

    @Test
    void shouldNotCreateOAuthUserWhenUserAlreadyExists() {
        // given
        String userEmail = "test@example.com";
        DefaultOAuth2User oauthUser = new DefaultOAuth2User(
                null,
                Map.of("name", "Test", "family_name", "User", "picture", "http://example.com/avatar.jpg"),
                "name"
        );

        User existingUser = new User();
        existingUser.setUserEmail(userEmail);

        given(userRepository.findByUserEmail(userEmail)).willReturn(Optional.of(existingUser));

        // when
        oAuthUserService.handleOAuthUser(userEmail, oauthUser);

        // then
        then(userRepository).should(never()).save(ArgumentMatchers.any());
    }

    @Test
    void shouldHandleExceptionWhenAvatarProcessingFails() throws Exception {
        // given
        String userEmail = "test@example.com";
        DefaultOAuth2User oauthUser = new DefaultOAuth2User(
                null,
                Map.of("name", "Test", "family_name", "User", "picture", "http://example.com/avatar.jpg"),
                "name"
        );

        given(userRepository.findByUserEmail(userEmail)).willReturn(Optional.empty());
        given(imageProcessor.getAvatarFromUrl("http://example.com/avatar.jpg")).willThrow(new RuntimeException("Processing error"));

        // when
        oAuthUserService.handleOAuthUser(userEmail, oauthUser);

        // then
        then(userRepository).should().save(argThat(user -> {
            assertThat(user.getUserEmail(), is(userEmail));
            assertThat(user.getUserFirstName(), is("Test"));
            assertThat(user.getUserLastName(), is("User"));
            assertThat(user.getPassword(), is(AuthProviders.GOOGLE_PROVIDER));
            assertThat(user.getUserAvatar(), is(nullValue()));
            assertThat(user.getVerified(), is(true));
            return true;
        }));
    }
}