package com.cleaningservice.exception;

public class CannotUpdateBookingException extends RuntimeException {
    public CannotUpdateBookingException(String message) {
        super(message);
    }
}
