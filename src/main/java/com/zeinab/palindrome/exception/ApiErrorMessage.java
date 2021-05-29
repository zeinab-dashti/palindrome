package com.zeinab.palindrome.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiErrorMessage {

    private int error;
    private String message;
    private String path;
    private HttpStatus status;
    private String timestamp;
}
