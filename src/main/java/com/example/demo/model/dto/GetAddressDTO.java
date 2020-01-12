package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAddressDTO {

    private Long id;
    private String city;
    private String phoneNumber;
    private String addressDetails;

}
