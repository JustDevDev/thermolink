package org.skomi.pilot.shared.exception;

/**
 * On failed authentication on wrong credentials.
 */
public class WrongCredentialsException extends RuntimeException {

    public WrongCredentialsException(String message) {
        super(message);
    }
}
