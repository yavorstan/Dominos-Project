package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ErrorCreatingEntityException extends Throwable {

    public ErrorCreatingEntityException(String message) {
        super(message);
    }

}
