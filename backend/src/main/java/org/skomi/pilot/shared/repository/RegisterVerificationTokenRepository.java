package org.skomi.pilot.shared.repository;

import org.skomi.pilot.shared.model.RegisterVerificationToken;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegisterVerificationTokenRepository extends CrudRepository<RegisterVerificationToken, String> {

    /**
     * Finds a RegisterVerificationToken based on the provided token string.
     *
     * @param token the token string used to identify the desired RegisterVerificationToken
     * @return an Optional containing the RegisterVerificationToken if found, or an empty Optional if no match is found
     */
    Optional<RegisterVerificationToken> findByToken(String token);

    /**
     * Inserts a new register verification token into the database.
     *
     * @param token   the verification token to be inserted
     * @param userId  the unique identifier of the user associated with this token
     * @param created the date and time when the token was created
     * @return the inserted {@link RegisterVerificationToken} object
     */
    @Query("INSERT INTO register_verification_token (token, user_id, created) VALUES (:token, :userId, :created) RETURNING *")
    RegisterVerificationToken insertToken(String token, UUID userId, OffsetDateTime created);
}