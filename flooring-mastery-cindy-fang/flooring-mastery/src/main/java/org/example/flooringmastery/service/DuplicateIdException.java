package org.example.flooringmastery.service;

public class DuplicateIdException extends RuntimeException {

    // constructor that accepts only a message
    public DuplicateIdException(String message) {
        super(message);
    }

    // constructor that accepts both a message and a cause
    public DuplicateIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
