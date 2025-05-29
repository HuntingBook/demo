package com.flightbooking.exception;

public class EmptyPassengerListException extends RuntimeException {
    public EmptyPassengerListException(String message) {
        super(message);
    }
}
