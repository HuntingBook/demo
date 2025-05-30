package com.flightapi.exception;

/**
 * Custom exception thrown when an attempt is made to register a new user with an email address
 * that already exists in the system.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    /**
     * Constructs a new {@code EmailAlreadyExistsException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
