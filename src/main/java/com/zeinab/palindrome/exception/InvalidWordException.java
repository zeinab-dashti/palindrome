package com.zeinab.palindrome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class InvalidWordException extends HttpStatusCodeException {

    private final String message;

    public InvalidWordException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}