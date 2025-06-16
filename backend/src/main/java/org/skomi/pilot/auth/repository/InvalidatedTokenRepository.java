package org.skomi.pilot.auth.repository;

import org.skomi.pilot.auth.model.InvalidatedToken;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

/**
 * Handling persistence blacklist of invalidated tokens
 */
public interface InvalidatedTokenRepository extends CrudRepository<InvalidatedToken, String> {

    /**
     * Checks if a token has been invalidated by verifying its hash and expiration time.
     *
     * @param tokenHash The hash of the token to check for invalidation.
     * @param now       The current time to compare with the token's expiration time.
     * @return true if the token is invalid (exists in the database and is not expired), otherwise false.
     */
    @Query("SELECT COUNT(*) > 0 FROM invalidated_token WHERE token_hash = :tokenHash AND expires_at > :now")
    boolean isTokenInvalidated(String tokenHash, OffsetDateTime now);

    /**
     * Deletes all invalidated tokens that have expired before the given timestamp.
     *
     * @param now The current time used to delete tokens that expired earlier.
     */
    @Modifying
    @Query("DELETE FROM invalidated_token WHERE expires_at < :now")
    void deleteExpiredTokens(OffsetDateTime now);

    /**
     * Inserts a new invalidated token into the database or updates the existing token's invalidation
     * timestamp and expiration timestamp if a conflict occurs on the token hash.
     *
     * @param token The invalidated token containing the token hash, invalidation timestamp,
     *              and expiration timestamp.
     */
    @Modifying
    @Query("""
            INSERT INTO invalidated_token (
                token_hash,
                invalidated_at,
                expires_at
            ) VALUES (
                :#{#token.tokenHash},
                :#{#token.invalidatedAt},
                :#{#token.expiresAt}
            )
            ON CONFLICT (token_hash)
            DO UPDATE SET
                invalidated_at = EXCLUDED.invalidated_at,
                expires_at = EXCLUDED.expires_at
            """)
    void upsertToken(@Param("token") InvalidatedToken token);
}