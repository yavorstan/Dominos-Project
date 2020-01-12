package com.example.demo.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class CustomError {

    private String exception;

    private String message;

    @JsonFormat(pattern = "dd-MM-yyyy  HH:mm:ss")
    private LocalDateTime time;

    private int status;

}
