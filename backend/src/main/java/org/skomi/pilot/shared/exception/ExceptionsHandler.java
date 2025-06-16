package org.skomi.pilot.shared.exception;

import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.shared.Messages;
import org.skomi.pilot.shared.model.ErrorResponse;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handling
 */
@RestControllerAdvice
@Slf4j
public class ExceptionsHandler {


    /**
     * Handles exceptions of type {@link WrongCredentialsException}.
     *
     * @param ex the exception thrown when the provided credentials are invalid
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} with an error message
     * and an HTTP status of 401 (Unauthorized)
     */
    @ExceptionHandler(WrongCredentialsException.class)
    public ResponseEntity<ErrorResponse> onWrongSortingParameterException(WrongCredentialsException ex) {
        log.error("Wrong credentials: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(Messages.MSG_WRONG_CREDENTIALS, Messages.MSG_WRONG_CREDENTIALS);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles exceptions of type {@link UserNotVerifiedException}.
     *
     * @param ex the exception thrown when the provided credentials are invalid
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} with an error message
     * and an HTTP status of 403 (Forbidden)
     */
    @ExceptionHandler(UserNotVerifiedException.class)
    public ResponseEntity<ErrorResponse> onUserNotVerifiedException(UserNotVerifiedException ex) {
        log.error("User not verified: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(Messages.MSG_USER_EMAIL_NOT_AUTHORIZED, Messages.MSG_USER_EMAIL_NOT_AUTHORIZED);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles exceptions of type {@link PropertyReferenceException}.
     *
     * @param ex the exception thrown when an invalid property or parameter is referenced
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} with an error message
     * and an HTTP status of 400 (Bad Request)
     */
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorResponse> onWrongSortingParameterException(PropertyReferenceException ex) {
        log.error("Wrong sorting parameter: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Wrong sorting parameter", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles runtime exceptions that are not specifically categorized.
     *
     * @param ex the runtime exception that was thrown
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} with a general error message
     *         and an HTTP status of 500 (Internal Server Error)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> onUnknownException(RuntimeException ex) {
        log.error("Full error: ", ex);
        log.error("Exception basic: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(Messages.MSG_SERVER_ERROR, Messages.MSG_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles exceptions of type {@link ForgottenPasswordTokenExpiredException}.
     *
     * @param ex the exception thrown when a user's forgotten password token has expired
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} with an error message
     *         and an HTTP status of 403 (Forbidden)
     */
    @ExceptionHandler(ForgottenPasswordTokenExpiredException.class)
    public ResponseEntity<ErrorResponse> onForgottenPasswordTokenExpired(ForgottenPasswordTokenExpiredException ex) {
        log.error("Forgotten password token expired: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(Messages.MSG_FORGOTTEN_PASSWORD_TOKEN_EXPIRED, Messages.MSG_FORGOTTEN_PASSWORD_TOKEN_EXPIRED);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles exceptions of type {@link UsernameAlreadyExistsException}.
     *
     * @param ex the exception thrown when attempting to register a username that already exists
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} with an error message
     *         and an HTTP status of 400 (Bad Request)
     */
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> onUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        log.error("Username already exists: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(Messages.MSG_USER_ALREADY_EXISTS, Messages.MSG_USER_ALREADY_EXISTS);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions of type {@link InvalidUserDataException}.
     *
     * @param ex the exception containing details about the invalid user data
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} with an error message
     *         and an HTTP status of 400 (Bad Request)
     */
    @ExceptionHandler(InvalidUserDataException.class)
    public ResponseEntity<ErrorResponse> onInvalidUserDataException(InvalidUserDataException ex) {
        log.error("Invalid user data: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(Messages.MSG_INVALID_DATA, Messages.MSG_INVALID_DATA);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
