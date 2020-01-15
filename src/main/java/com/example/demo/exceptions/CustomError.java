package com.example.demo.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class CustomError {

    private String exception;

    private int status;

    private String message;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime time;

    public CustomError(String exception, String message, LocalDateTime time, int status) {
        this.exception = exception;
        this.status = status;
        this.message = message;
        this.time = time;
    }
}
