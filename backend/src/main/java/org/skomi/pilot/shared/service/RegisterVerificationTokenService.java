package org.skomi.pilot.shared.service;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.shared.model.RegisterVerificationToken;
import org.skomi.pilot.shared.repository.RegisterVerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterVerificationTokenService {

    private final RegisterVerificationTokenRepository tokenRepository;

    /**
     * Retrieves a {@link RegisterVerificationToken} based on its token value.
     *
     * @param token the token string used to retrieve the corresponding RegisterVerificationToken
     * @return an Optional containing the RegisterVerificationToken if found, or an empty Optional if no token is found
     */
    public Optional<RegisterVerificationToken> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    /**
     * Creates a new verification token, associates it with a user, persists it in the repository,
     * and returns the generated token as a string.
     * <p>
     * The method generates a token using UUID, creates a RegisterVerificationToken entity with
     * the generated token, current timestamp, and a randomly generated userId. The token entity
     * is then saved in the repository.
     *
     * @return the generated verification token as a String
     */
    public String createAndSaveToken(UUID userId) {

        RegisterVerificationToken tokenEntity = new RegisterVerificationToken();
        tokenEntity.setToken(UUID.randomUUID().toString());
        tokenEntity.setUserId(userId);
        tokenEntity.setCreated(OffsetDateTime.now());

        // save token
        RegisterVerificationToken savedToken = tokenRepository.insertToken(tokenEntity.getToken(), tokenEntity.getUserId(), tokenEntity.getCreated());
        return savedToken.getToken();
    }
}