package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

}
