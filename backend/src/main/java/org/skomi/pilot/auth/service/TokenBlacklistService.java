package org.skomi.pilot.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.auth.model.InvalidatedToken;
import org.skomi.pilot.auth.repository.InvalidatedTokenRepository;
import org.springframework.modulith.NamedInterface;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@NamedInterface("TokenBlacklistService")
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final InvalidatedTokenRepository tokenRepository;
    private final JwtTokenService jwtTokenService;

    /**
     * Invalidates a given JWT token by creating a hashed representation of the token,
     * storing it in the repository, and associating it with its expiration time.
     *
     * @param token the JWT token to invalidate
     */
    public void invalidateToken(String token) {
        try {
            String tokenHash = hashToken(token);
            Date expiration = jwtTokenService.getExpirationDateFromToken(token);

            InvalidatedToken invalidatedToken = new InvalidatedToken();
            invalidatedToken.setTokenHash(tokenHash);
            invalidatedToken.setInvalidatedAt(OffsetDateTime.now());
            invalidatedToken.setExpiresAt(OffsetDateTime.ofInstant(
                    expiration.toInstant(),
                    ZoneId.systemDefault()
            ));

            tokenRepository.upsertToken(invalidatedToken);
            log.debug("Token invalidated successfully");
        } catch (Exception e) {
            log.error("Failed to invalidate token", e);
        }
    }

    /**
     * Checks if a given JWT token has been invalidated by comparing its hash to the repository.
     *
     * @param token the JWT token to check
     * @return true if the token is invalidated, false otherwise
     */
    public boolean isTokenInvalidated(String token) {
        try {
            String tokenHash = hashToken(token);
            return tokenRepository.isTokenInvalidated(tokenHash, OffsetDateTime.now());
        } catch (Exception e) {
            log.error("Failed to check token status", e);
            return true;
        }
    }

    /**
     * Converts a JWT token into a secure hash using SHA-256 and encodes it in Base64.
     *
     * @param token the JWT token to hash
     * @return a Base64 encoded SHA-256 hash of the token
     * @throws SecurityException if the hashing algorithm is not available
     */
    private String hashToken(String token) {
        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException("Failed to hash token", e);
        }
    }

    /**
     * Periodically removes all expired tokens from the repository. Runs every hour.
     */
    @Scheduled(cron = "0 0 * * * *") // Run hourly
    public void cleanupExpiredTokens() {
        try {
            tokenRepository.deleteExpiredTokens(OffsetDateTime.now());
        } catch (Exception e) {
            log.error("Failed to cleanup expired tokens", e);
        }
    }
}