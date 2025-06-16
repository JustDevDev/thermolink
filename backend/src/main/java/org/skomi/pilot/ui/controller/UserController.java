package org.skomi.pilot.ui.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.skomi.pilot.auth.service.JwtTokenService;
import org.skomi.pilot.shared.Emails;
import org.skomi.pilot.shared.exception.ForgottenPasswordTokenExpiredException;
import org.skomi.pilot.shared.model.RegisterVerificationToken;
import org.skomi.pilot.shared.model.User;
import org.skomi.pilot.shared.publisher.EmailPublisher;
import org.skomi.pilot.shared.repository.UserRepository;
import org.skomi.pilot.shared.service.CookiesService;
import org.skomi.pilot.shared.service.ImageProcessor;
import org.skomi.pilot.shared.service.RegisterVerificationTokenService;
import org.skomi.pilot.shared.service.UserService;
import org.skomi.pilot.ui.model.UserEmailDto;
import org.skomi.pilot.ui.model.UserNewPasswordDTO;
import org.skomi.pilot.ui.model.UserRegistrationRequest;
import org.skomi.pilot.ui.model.UserUpdateRequestDTO;
import org.skomi.pilot.ui.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CookiesService cookiesService;
    private final JwtTokenService jwtTokenService;
    private final ForgotPasswordService forgotPasswordService;
    private final RegisterVerificationTokenService registerVerificationTokenService;
    private final EmailPublisher emailPublisher;
    private final UserRepository userRepository;
    private final ImageProcessor imageProcessor;
    @Value("${user.validate.url:http://localhost:3001/verify-account}")
    private String validateUserUrl;

    /**
     * Registers a new user in the system using the provided user registration request.
     * The method creates a new user based on the input data, encrypts the password, and saves
     * the user in the database.
     *
     * @param request the {@link UserRegistrationRequest} object containing the email and password
     *                for the new user
     * @return a {@link ResponseEntity} containing the saved {@link User} object if the registration
     * is successful
     */
    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Username already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        User newUser = new User();
        newUser.setUserEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setVerified(false);
        newUser.setHintPassed(false);

        User savedUser = userService.createUser(newUser);

        // generate verification token and save it to db
        String generatedToken = registerVerificationTokenService.createAndSaveToken(savedUser.getId());

        emailPublisher.sendEmail(
                savedUser.getUserEmail(),
                Emails.EMAIL_VERIFY_USER_EMAIL,
                validateUserUrl,
                generatedToken
        );

        return ResponseEntity.ok(savedUser);
    }

    /**
     * Updates the properties of a user. The method retrieves the JWT token from the provided
     * cookies in the HTTP request to identify the currently authenticated user. The email
     * extracted from the token is used to update the user's data with the information provided
     * in the request DTO.
     *
     * @param request            the {@link UserUpdateRequestDTO} containing the new user data to update,
     *                           such as first name, last name, avatar, and password
     * @param httpServletRequest the {@link HttpServletRequest} containing the cookies where
     *                           the JWT token is stored
     * @return a {@link ResponseEntity} with no content if the operation is successful
     */
    @Operation(description = "Update user properties")
    @PutMapping("/user")
    public ResponseEntity<Void> updateUserProperties(
            @RequestBody UserUpdateRequestDTO request,
            HttpServletRequest httpServletRequest
    ) throws IOException {
        String token = cookiesService.extractJwtFromCookies(httpServletRequest);
        String userEmail = jwtTokenService.getUserEmailFromToken(token);
        String processedAvatar = null;

        if (Objects.nonNull(request.userAvatar()) && !request.userAvatar().isBlank()) {
            processedAvatar = imageProcessor.processAvatarImage(request.userAvatar(), 128);
        }

        userService.updateUserData(
                userEmail,
                request.currentPassword(),
                request.newPassword(),
                processedAvatar,
                request.userFirstName(),
                request.userLastName(),
                request.hintPassed()
        );

        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to initiate a forgotten password process.
     * It generates a token and sends a reset URL via email.
     *
     * @param email the user email requesting a password reset
     * @return a 200 status if the email is accepted
     */
    @Operation(summary = "Initiate reset password process")
    @PostMapping("/user/forgottenPassword")
    public ResponseEntity<Void> initiateForgottenPasswordProcess(@RequestBody UserEmailDto email) {
        forgotPasswordService.generateAndSendEmailWithReset(email.email());
        // For security. Return 200 even if the user is not found.
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to validate the token. Returns 204 if valid,
     * or 404 if the token is invalid or expired.
     *
     * @param token the token part of the reset URL
     * @return 204 (No Content) if valid, 404 (Not Found) otherwise
     */
    @Operation(summary = "Validate forgot password token")
    @GetMapping("/user/forgottenPassword/{token}/validate")
    public ResponseEntity<Void> validateForgottenPasswordTokenValid(@PathVariable String token) {
        if (forgotPasswordService.isTokenValid(token)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ForgottenPasswordTokenExpiredException();
        }
    }

    /**
     * Updates the user's password after validating the token associated with the forgotten password process.
     * If the user is found with the provided email, the new password is set after encoding it.
     *
     * @param userNewPasswordDTO the {@link UserNewPasswordDTO} object containing the user's email and new password
     * @return a {@link ResponseEntity} with no content if the operation is successful
     * @throws RuntimeException if the user is not found or an unexpected error occurs
     */
    @Operation(summary = "Set new password to the user.")
    @PostMapping("/user/forgottenPassword/newPassword")
    public ResponseEntity<Void> validateForgottenPasswordTokenValid(
            @RequestBody UserNewPasswordDTO userNewPasswordDTO
    ) {
        userService.findUserByResetPasswordToken(userNewPasswordDTO.resetPasswordToken()).ifPresentOrElse(
                user -> {
                    user.setPassword(passwordEncoder.encode(userNewPasswordDTO.password()));
                    userRepository.save(user);
                },
                () -> {
                    throw new RuntimeException("Something got wrong.");
                }
        );

        return ResponseEntity.noContent().build();
    }

    /**
     * Validates the account verification token for a given user email. If the token is valid,
     * the method returns a 204 (No Content) status. If the token is invalid or the user cannot
     * be found by the email, it returns a 404 (Not Found) status.
     *
     * @param token the token used for account verification
     * @return a {@link ResponseEntity} with a 204 (No Content) status if the token is valid,
     * or a 404 (Not Found) status if the token is invalid or the user does not exist
     */
    @Operation(summary = "Verify account of new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account successfully verified"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/verifyAccount/{token}/validate")
    public ResponseEntity<Void> validateVerifyAccountToken(
            @PathVariable String token
    ) {

        // check if the token is valid for the given user id
        Optional<RegisterVerificationToken> tokenData = registerVerificationTokenService.getToken(token);
        if (tokenData.isPresent()) {
            userService.updateUserVerified(tokenData.get().getUserId());
            return ResponseEntity.noContent().build();

        } else {
            return ResponseEntity.notFound().build();

        }
    }
}