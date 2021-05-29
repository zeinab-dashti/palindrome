package com.zeinab.palindrome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class AuthenticationException extends HttpStatusCodeException {

    private final String message;

    public AuthenticationException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
