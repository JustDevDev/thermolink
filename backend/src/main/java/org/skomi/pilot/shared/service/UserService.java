package org.skomi.pilot.shared.service;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.shared.exception.InvalidUserDataException;
import org.skomi.pilot.shared.exception.UsernameAlreadyExistsException;
import org.skomi.pilot.shared.model.User;
import org.skomi.pilot.shared.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing user-related operations including creation, updates and queries.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new user after validation and checking for existing username.
     *
     * @param user the user object containing the user details to be created
     * @return the created user entity
     * @throws InvalidUserDataException       if the user data is invalid
     * @throws UsernameAlreadyExistsException if a user with the same email already exists
     */
    @Transactional
    public User createUser(User user) {
        validateUser(user);

        if (userRepository.findByUserEmail(user.getUserEmail()).isPresent()) {
            throw new UsernameAlreadyExistsException(
                    "Username '" + user.getUserEmail() + "' is already taken"
            );
        }

        return userRepository.save(user);
    }

    /**
     * Updates the user data including password, avatar, first name, and last name.
     * Throws exceptions if the current password is incorrect or if any other mandatory conditions are violated.
     *
     * @param userEmail       the email address of the user whose data is being updated
     * @param currentPassword the current password of the user, required for updating the password
     * @param newPassword     the new password for the user, will replace the current password if provided
     * @param newAvatar       the new avatar image in Base64 encoding, will update the user's avatar if provided
     * @param newFirstName    the new first name of the user, will update the user's first name if provided
     * @param newLastName     the new last name of the user, will update the user's last name if provided
     * @param hintPassed      boolean indicating if user has passed all hints
     * @throws InvalidUserDataException if the current password is incorrect when attempting to update the password
     */
    @Transactional
    public void updateUserData(String userEmail, String currentPassword, String newPassword, String newAvatar, String newFirstName, String newLastName, Boolean hintPassed) {
        User currentUser = userRepository.findByUserEmail(userEmail).orElseThrow();

        // password change
        if (currentPassword != null && newPassword != null) {
            if (!passwordEncoder.matches(currentPassword, currentUser.getPassword())) {
                throw new InvalidUserDataException("Current password is incorrect");
            }
            currentUser.setPassword(passwordEncoder.encode(newPassword));
        }

        if (newAvatar != null) {
            currentUser.setUserAvatar(newAvatar);
        }

        if (newFirstName != null) {
            currentUser.setUserFirstName(newFirstName);
        }

        if (newLastName != null) {
            currentUser.setUserLastName(newLastName);
        }

        if (hintPassed != null) {
            currentUser.setHintPassed(hintPassed);
        }

        userRepository.save(currentUser);
    }

    /**
     * Updates the verification status of a user to "verified" if the user exists in the repository.
     *
     * @param userId the unique identifier of the user whose verification status is to be updated
     */
    public void updateUserVerified(UUID userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setVerified(true);
            userRepository.save(user);
        });
    }

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user to search for
     * @return an Optional containing the User if found, or an empty Optional if no user with the given email exists
     */
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

    /**
     * Retrieves a user based on the provided reset password token.
     *
     * @param resetPasswordToken the reset password token used to find the user
     * @return an Optional containing the User if a match is found, or an empty Optional if no user is found
     */
    public Optional<User> findUserByResetPasswordToken(String resetPasswordToken) {
        // get user by reset password token
        return userRepository.findByResetPasswordToken(resetPasswordToken);
    }

    /**
     * Validates the user object to ensure it meets required conditions.
     * Throws exceptions if the user object or its required attributes are invalid.
     *
     * @param user the user object to be validated
     * @throws InvalidUserDataException if the user object is null, or if the email or password is null or empty
     */
    private void validateUser(User user) {
        if (user == null) {
            throw new InvalidUserDataException("User cannot be null");
        }
        if (user.getUserEmail() == null || user.getUserEmail().trim().isEmpty()) {
            throw new InvalidUserDataException("Username cannot be null or empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new InvalidUserDataException("Password cannot be null or empty");
        }
    }
}