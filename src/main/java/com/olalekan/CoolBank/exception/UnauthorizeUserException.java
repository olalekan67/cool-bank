package com.olalekan.CoolBank.exception;

public class UnauthorizeUserException extends RuntimeException {
    public UnauthorizeUserException(String message) {
        super(message);
    }
}
