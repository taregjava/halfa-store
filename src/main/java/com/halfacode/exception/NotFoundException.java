package com.halfacode.exception;

public class NotFoundException extends RuntimeException{
    private String errorMessage;

    public NotFoundException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
