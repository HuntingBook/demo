package com.flightapi.exception;

public class EmptyPassengerListException extends RuntimeException {
    public EmptyPassengerListException(String message) {
        super(message);
    }
}
