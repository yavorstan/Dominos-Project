package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ElementAlreadyExistsException extends Exception {

    public ElementAlreadyExistsException(String s) {
        super(s);
    }

}
