package com.alura.restapiforohubchallenge.exceptions.exceptions;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
