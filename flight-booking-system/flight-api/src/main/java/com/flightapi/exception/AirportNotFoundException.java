package com.flightapi.exception;

/**
 * Custom exception thrown when an airport cannot be found in the system.
 * This typically occurs when a search or lookup operation for an airport by its code or ID fails.
 */
public class AirportNotFoundException extends RuntimeException {
    /**
     * Constructs a new {@code AirportNotFoundException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public AirportNotFoundException(String message) {
        super(message);
    }
}
