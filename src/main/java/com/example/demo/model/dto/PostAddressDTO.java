package com.example.demo.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PostAddressDTO {

    @NotBlank(message = "City is mandatory!")
    private String city;

    @NotBlank(message = "Phone number is mandatory!")
    private String phoneNumber;

    @NotBlank(message = "Address details are needed!")
    private String addressDetails;

}
