package com.flightapi.exception;

/**
 * Custom exception thrown when a flight cannot be found in the system.
 * This typically occurs when a search or lookup operation for a flight by its ID or other criteria fails.
 */
public class FlightNotFoundException extends RuntimeException {
    /**
     * Constructs a new {@code FlightNotFoundException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public FlightNotFoundException(String message) {
        super(message);
    }
}
