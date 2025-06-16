package org.skomi.pilot.shared.repository;

import org.skomi.pilot.shared.model.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link User} entities.
 */
public interface UserRepository extends CrudRepository<User, UUID> {

    /**
     * Finds a User by their email.
     *
     * @param username the email of the user to search for
     * @return an Optional containing the User if found, or an empty Optional if no user with the given email exists
     */
    @Query("SELECT * FROM app_user WHERE user_email = :username")
    Optional<User> findByUserEmail(String username);

    /**
     * Finds a User based on the provided reset password token.
     *
     * @param token the reset password token used to find the associated user
     * @return an Optional containing the User if a match is found, or an empty Optional if no user is associated with the provided token
     */
    @Query("""
            SELECT au.* FROM reset_password_token rpt
                JOIN app_user au ON au.id = rpt.user_id
            WHERE rpt.token = :token
            """)
    Optional<User> findByResetPasswordToken(String token);
}
