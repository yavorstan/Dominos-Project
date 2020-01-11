package com.example.demo.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class PostUserRegistrationFormDto extends AbstractUserDto {
    @NotBlank
    private String confirmationEmail;
    @NotBlank
    private String confirmationPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
