package org.example.flooringmastery.dao;

public class PersistenceException extends RuntimeException {

    // constructor that accepts only a message
    public PersistenceException(String message) {
        super(message);
    }

    // constructor that accepts both a message and a cause
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
