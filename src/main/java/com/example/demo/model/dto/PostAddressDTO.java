package com.example.demo.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PostAddressDTO {

    @NotBlank(message = "City is mandatory!")
    private String city;

    @NotBlank(message = "Phone number is mandatory!")
    @Length(min = 7, max = 10, message = "Please enter valid phone number!")
    private String phoneNumber;

    @NotBlank(message = "Address details are needed!")
    private String addressDetails;

}
