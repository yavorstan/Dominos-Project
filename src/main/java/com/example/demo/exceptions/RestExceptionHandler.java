package com.example.demo.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@EnableWebMvc
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ElementAlreadyExistsException.class, ErrorCreatingEntityException.class})
    public ResponseEntity<Object> handleBadRequest(RuntimeException exception) {
        Error error = new Error(exception.getMessage());
        return new ResponseEntity<>(error.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ElementNotFoundException.class})
    public ResponseEntity<Object> handleElementNotFoundException(RuntimeException exception) {
        Error error = new Error(exception.getMessage());
        return new ResponseEntity<>(error.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

}
