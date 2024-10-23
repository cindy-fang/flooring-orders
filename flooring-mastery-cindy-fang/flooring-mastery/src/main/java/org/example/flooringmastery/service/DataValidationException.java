package org.example.flooringmastery.service;

public class DataValidationException extends RuntimeException {

    // constructor that accepts only a message
    public DataValidationException(String message) {
        super(message);
    }

    // constructor that accepts both a message and a cause
    public DataValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
