package com.flightapi.exception;

/**
 * Custom exception thrown when a date string provided by the user does not conform to the expected format (e.g., "yyyy-MM-dd").
 */
public class InvalidDateFormatException extends RuntimeException {
    /**
     * Constructs a new {@code InvalidDateFormatException} with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method).
     */
    public InvalidDateFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code InvalidDateFormatException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public InvalidDateFormatException(String message) {
        super(message);
    }
}
