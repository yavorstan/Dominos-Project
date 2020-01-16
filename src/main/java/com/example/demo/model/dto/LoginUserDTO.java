package com.example.demo.model.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class LoginUserDTO {

    @NotNull(message = "Invalid email and/or password!")
    private String email;

    @NotNull(message = "Invalid email and/or password!")
    private String password;

}
