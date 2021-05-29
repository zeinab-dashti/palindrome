package com.zeinab.palindrome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class ConflictUserException extends HttpStatusCodeException {
    private final String message;

    public ConflictUserException(String message) {
        super(HttpStatus.CONFLICT, message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
