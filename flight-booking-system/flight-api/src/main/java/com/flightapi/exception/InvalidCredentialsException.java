package com.flightapi.exception;

/**
 * Custom exception thrown when a user attempts to log in with invalid credentials (e.g., incorrect email or password).
 */
public class InvalidCredentialsException extends RuntimeException {
    /**
     * Constructs a new {@code InvalidCredentialsException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
