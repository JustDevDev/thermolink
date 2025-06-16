package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.shared.Emails;
import org.skomi.pilot.shared.model.User;
import org.skomi.pilot.shared.publisher.EmailPublisher;
import org.skomi.pilot.shared.service.UserService;
import org.skomi.pilot.ui.model.ResetPasswordToken;
import org.skomi.pilot.ui.repository.ResetPasswordTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForgotPasswordService {

    private final UserService userService;
    // In-memory token storage: token -> expirationTime
    Map<String, LocalDateTime> tokenHashmap = new HashMap<>();

    private final EmailPublisher emailPublisher;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    @Value("${user.forgotpassword.url:http://localhost:3001/forgotten-password/%s}")
    private String resetBaseUrl;

    /**
     * Generates a password reset token, constructs the reset email, and sends it to the specified user email
     * address if the user exists in the system. If the user does not exist, the method silently ignores the
     * operation for security reasons.
     *
     * @param userEmail the email address of the user requesting a password reset
     */
    public void generateAndSendEmailWithReset(String userEmail) {
        // Verify that user exists.
        Optional<User> user = userService.findUserByEmail(userEmail);
        if (user.isEmpty()) {
            // for security reasons silently ignore unknown emails.
            log.info("User not found for email: {}", userEmail);
            return;
        }

        String resetUrl = generateForgotPasswordTokenUrl(userEmail);

        emailPublisher.sendEmail(
                userEmail,
                Emails.EMAIL_RESET_PASSWORD,
                resetUrl
        );
    }

    /**
     * Generate a token for a given user email. The token will be valid for 15 minutes.
     * An email with the reset URL is sent out.
     *
     * @return the generated token
     */
    public String generateForgotPasswordTokenUrl(String userEmail) {

        String token = UUID.randomUUID().toString();
        LocalDateTime created = LocalDateTime.now();
        User user = userService.findUserByEmail(userEmail).orElseThrow();

        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.setToken(token);
        resetPasswordToken.setUserId(user.getId());
        resetPasswordToken.setCreated(created);
        resetPasswordTokenRepository.upsert(resetPasswordToken);

        tokenHashmap.put(token, created.plusMinutes(15));

        // construct reset URL
        return resetBaseUrl.formatted(token);
    }

    /**
     * Validate the provided token. It returns true if the token exists and has not expired.
     *
     * @param token the token to validate
     * @return true if valid, false otherwise.
     */
    public boolean isTokenValid(String token) {
        LocalDateTime expiration = tokenHashmap.get(token);
        if (expiration == null || LocalDateTime.now().isAfter(expiration)) {
            // clean up expired tokens
            tokenHashmap.remove(token);
            return false;
        }
        return true;
    }
}