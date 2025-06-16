package org.skomi.pilot.shared.exception;

import lombok.RequiredArgsConstructor;

/**
 * On failed authentication on wrong credentials.
 */
@RequiredArgsConstructor
public class UserNotVerifiedException extends RuntimeException {
}
