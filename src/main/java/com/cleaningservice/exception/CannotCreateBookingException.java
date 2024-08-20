package com.cleaningservice.exception;

public class CannotCreateBookingException extends RuntimeException {
    public CannotCreateBookingException(String message) {
        super(message);
    }
}
