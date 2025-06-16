package org.skomi.pilot.shared.service;

import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.shared.AuthProviders;
import org.skomi.pilot.shared.model.User;
import org.skomi.pilot.shared.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class OAuthUserService extends UserService {

    private final ImageProcessor imageProcessor;

    public OAuthUserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ImageProcessor imageProcessor) {
        super(userRepository, passwordEncoder);
        this.imageProcessor = imageProcessor;
    }

    /**
     * Handles the registration of an OAuth user. If no user exists with the provided email,
     * a new user is created and stored in the database. The user details are populated
     * with attributes from the provided OAuth2 user.
     *
     * @param userEmail the email address of the user to be processed
     * @param oauthUser the DefaultOAuth2User containing user details from the OAuth authProvider
     */
    @Transactional
    public void handleOAuthUser(String userEmail, DefaultOAuth2User oauthUser) {
        // register new user if exists
        if (findUserByEmail(userEmail).isEmpty()) {
            User userToCreate = new User();
            userToCreate.setUserEmail(userEmail);
            userToCreate.setUserFirstName(oauthUser.getAttribute("name"));
            userToCreate.setUserLastName(oauthUser.getAttribute("family_name"));
            userToCreate.setPassword(AuthProviders.GOOGLE_PROVIDER);
            userToCreate.setVerified(true);
            userToCreate.setHintPassed(false);

            String avatarUrl = oauthUser.getAttribute("picture");
            String avatarBase64 = null;

            try {
                avatarBase64 = imageProcessor.getAvatarFromUrl(avatarUrl);
            } catch (Exception e) {
                log.error("Error while processing avatar from url: {}", avatarUrl, e);
            }

            userToCreate.setUserAvatar(avatarBase64);

            // create user
            createUser(userToCreate);
        }
    }
}
