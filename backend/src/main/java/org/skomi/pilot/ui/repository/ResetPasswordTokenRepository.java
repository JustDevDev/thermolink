package org.skomi.pilot.ui.repository;

import org.skomi.pilot.ui.model.ResetPasswordToken;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ResetPasswordTokenRepository extends Repository<ResetPasswordToken, UUID> {

    /**
     * Inserts or updates a ResetPasswordToken record in the database. If a record with the same user_id already exists,
     * it updates the existing record's token and created fields. If no record exists, a new record is inserted.
     *
     * @param token The ResetPasswordToken object containing the token, created timestamp, and user_id.
     */
    @Modifying
    @Query("INSERT INTO reset_password_token (token, created, user_id) " +
            "VALUES (:#{#token.token}, :#{#token.created}, :#{#token.userId}) " +
            "ON CONFLICT (user_id) DO UPDATE SET " +
            "token = :#{#token.token}, " +
            "created = :#{#token.created}")
    void upsert(@Param("token") ResetPasswordToken token);
}