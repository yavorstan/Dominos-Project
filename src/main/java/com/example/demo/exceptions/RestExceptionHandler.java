package com.example.demo.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@EnableWebMvc
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {ElementAlreadyExistsException.class})
    public ResponseEntity<Object> handleBadRequest(RuntimeException exception) {
        CustomError error = new CustomError(exception.getClass().getSimpleName(), exception.getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ErrorCreatingEntityException.class})
    public ResponseEntity<Object> handleErrorCreatingEntityException(RuntimeException exception) {
        CustomError error = new CustomError(exception.getClass().getSimpleName(), exception.getMessage(), LocalDateTime.now(), HttpStatus.UNPROCESSABLE_ENTITY.value());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = {ElementNotFoundException.class})
    public ResponseEntity<Object> handleElementNotFoundException(RuntimeException exception) {
        CustomError error = new CustomError(exception.getClass().getSimpleName(), exception.getMessage(), LocalDateTime.now(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {UnauthorizedAccessException.class})
    public ResponseEntity<Object> handleUnauthorizedAccessException(RuntimeException exception) {
        CustomError error = new CustomError(exception.getClass().getSimpleName(), exception.getMessage(), LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

}
