package com.example.demo.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostRegistrationFormDto {
    private String email;
    private String confirmationEmail;
    private String password;
    private String confirmationPassword;
    private String firstName;
    private String lastName;

}
