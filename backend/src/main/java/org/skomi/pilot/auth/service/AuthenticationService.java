package org.skomi.pilot.auth.service;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.shared.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    /**
     * Dependencies
     */
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticates a user based on the provided email and password.
     * If the authentication is successful, a JWT token is generated for the user.
     *
     * @param email the email of the user attempting to authenticate
     * @param password the password of the user attempting to authenticate
     * @return an Optional containing the generated JWT token if authentication is successful, or an empty Optional otherwise
     */
    public Optional<String> authenticate(String email, String password) {
        return userRepository.findByUserEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> jwtTokenService.createToken(email));
    }
}
