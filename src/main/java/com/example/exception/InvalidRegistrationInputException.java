package com.example.exception;

public class InvalidRegistrationInputException extends RuntimeException {

    public InvalidRegistrationInputException(String message) {
        super(message);
    }
    
}
