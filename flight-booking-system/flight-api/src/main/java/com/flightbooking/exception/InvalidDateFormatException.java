package com.flightbooking.exception;

public class InvalidDateFormatException extends RuntimeException {
    public InvalidDateFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDateFormatException(String message) {
        super(message);
    }
}
