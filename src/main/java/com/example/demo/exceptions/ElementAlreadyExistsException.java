package com.example.demo.exceptions;

public class ElementAlreadyExistsException extends RuntimeException {

    public ElementAlreadyExistsException(String message) {
        super(message);
    }

}
