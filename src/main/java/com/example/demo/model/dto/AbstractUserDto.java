package com.example.demo.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public abstract class AbstractUserDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
