package org.skomi.pilot.auth.config;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.auth.service.CustomOAuth2SuccessHandler;
import org.skomi.pilot.auth.service.JwtTokenService;
import org.skomi.pilot.auth.service.TokenBlacklistService;
import org.skomi.pilot.shared.service.CookiesService;
import org.skomi.pilot.shared.service.OAuthUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.spec.SecretKeySpec;

/**
 * Configuration class responsible for setting up the application's security mechanisms.
 * Configures HTTP security settings, authentication and authorization, and JWT handling.
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.frontend.url:http://localhost:3001/user/dashboard}")
    private String frontendUrl;

    /**
     * Dependencies
     */
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtTokenService jwtTokenService;

    /**
     * Configures the HTTP security filters for the application.
     * This regulates which endpoints are protected and the order of authentication filters.
     *
     * @param http           the {@link HttpSecurity} object to customize security configurations.
     * @param cookiesService the {@link CookiesService} used for managing cookies in the JWT filter.
     * @return the configured {@link SecurityFilterChain}.
     * @throws Exception if there's an error in the configuration process.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CookiesService cookiesService,
            OAuthUserService oAuthUserService) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // exclude docs from auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/ws/diagram",
                                "/api/login",
                                "/api/register",
                                "/api/verifyAccount/**",
                                "/api/user/forgottenPassword/**"
                        ).permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(tokenBlacklistService, jwtTokenService, cookiesService), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 ->
                        oauth2.successHandler(new CustomOAuth2SuccessHandler(
                                frontendUrl,
                                jwtTokenService,
                                cookiesService,
                                oAuthUserService))
                );

        return http.build();
    }

    /**
     * Creates a JWT decoder used for verifying the authenticity of JWT tokens.
     *
     * @param jwtTokenService the {@link JwtTokenService} providing key details for decoding JWTs.
     * @return an instance of {@link JwtDecoder} initialized with the application's secret key.
     */
    @Bean
    public JwtDecoder jwtDecoder(JwtTokenService jwtTokenService) {
        byte[] keyBytes = jwtTokenService.getKey().getEncoded();
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }
}
