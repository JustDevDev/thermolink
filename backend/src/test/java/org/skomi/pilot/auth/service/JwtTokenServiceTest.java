package org.skomi.pilot.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

    private SecretKey mockKey;

    @InjectMocks
    private JwtTokenService jwtTokenService;

    private String secretKeyString = "ExampleSecretKeyExampleSecretKeyExampleSecretKeyXXXXXXXXXXXXXXXXXXXXXXX";


    @BeforeEach
    void initKey() {
        mockKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
        jwtTokenService.setKey(mockKey);
    }

    @Test
    void shouldCreateTokenGivenValidEmail() {
        // given
        String email = "test@example.com";
        Instant now = Instant.now();
        Instant expiry = now.plus(1, ChronoUnit.HOURS);

        // when
        String token = jwtTokenService.createToken(email);

        // then
        assertThat(token, is(notNullValue()));

        var claims = Jwts.parser()
                .verifyWith(jwtTokenService.getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertThat(claims.getSubject(), is(email));
        assertThat(claims.getIssuedAt(), is(notNullValue()));
        assertThat(claims.getExpiration(), is(notNullValue()));
        assertThat(claims.getIssuedAt().toInstant(), is(lessThanOrEqualTo(expiry.minus(1, ChronoUnit.HOURS))));
    }
}