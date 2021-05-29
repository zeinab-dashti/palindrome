package com.zeinab.palindrome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private String dateInStringFormat(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
    @ExceptionHandler(InvalidWordException.class)
    public ResponseEntity<Object> socialFeedHandleNotFoundException(
            InvalidWordException ex, WebRequest request) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false),
                HttpStatus.BAD_REQUEST,
                dateInStringFormat());
        return new ResponseEntity<>(apiErrorMessage, apiErrorMessage.getStatus());
    }

    @ExceptionHandler(ConflictUserException.class)
    public ResponseEntity<Object> socialFeedHandleConflictException(
            ConflictUserException ex, WebRequest request) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getDescription(false),
                HttpStatus.CONFLICT,
                dateInStringFormat());
        return new ResponseEntity<>(apiErrorMessage, apiErrorMessage.getStatus());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> socialFeedHandleAuthenticationException(
            AuthenticationException ex, WebRequest request) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                request.getDescription(false),
                HttpStatus.UNAUTHORIZED,
                dateInStringFormat());
        return new ResponseEntity<>(apiErrorMessage, apiErrorMessage.getStatus());
    }
}
