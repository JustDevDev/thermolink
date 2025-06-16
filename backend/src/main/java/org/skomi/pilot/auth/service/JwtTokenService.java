package org.skomi.pilot.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Service responsible for creating JWT tokens.
 * <p>
 * This class provides functionality to generate JSON Web Tokens (JWT) based on user-specific data.
 * The tokens are signed using the HMAC SHA-256 algorithm and include a limited validity period.
 * <p>
 * In a production environment, ensure the secret key is securely managed through environment variables or a secure vault.
 */
@NamedInterface("JwtTokenService")
@Service
public class JwtTokenService {


    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY_STRING;
    @Getter
    @Setter
    private SecretKey key;

    /**
     * Initializes the secret key used for signing and verifying JWT tokens.
     * <p>
     * The key is derived from the configured secret string.
     */
    @PostConstruct
    void initKey() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());
    }

    /**
     * Generates a JWT token for the specified email.
     * <p>
     * The token includes the email as the subject, an issued timestamp, an expiration time
     * one hour after creation, and is signed with the configured secret key.
     *
     * @param email the email address for which the token is created
     * @return the generated JWT token as a string
     */
    public String createToken(String email) {
        Instant now = Instant.now();
        Instant expiry = now.plus(1, ChronoUnit.HOURS);

        return Jwts.builder()
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(key)
                .compact();
    }

    /**
     * Extracts the expiration date from a given JWT token.
     * <p>
     * The token is parsed and verified using the configured secret key.
     *
     * @param token the JWT token to extract the expiration date from
     * @return the expiration date of the token
     */
    public Date getExpirationDateFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    /**
     * Extracts the email (subject) from a given JWT token.
     * <p>
     * The token is parsed and verified using the configured secret key.
     *
     * @param token the JWT token to extract the email from
     * @return the email (subject) extracted from the token
     */
    public String getUserEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
}
