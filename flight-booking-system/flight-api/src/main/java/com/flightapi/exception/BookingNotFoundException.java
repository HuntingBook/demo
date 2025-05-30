package com.flightapi.exception;

/**
 * Custom exception thrown when a booking cannot be found in the system.
 * This typically occurs when a search or lookup operation for a booking by its reference or ID fails.
 */
public class BookingNotFoundException extends RuntimeException {
    /**
     * Constructs a new {@code BookingNotFoundException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public BookingNotFoundException(String message) {
        super(message);
    }
}
