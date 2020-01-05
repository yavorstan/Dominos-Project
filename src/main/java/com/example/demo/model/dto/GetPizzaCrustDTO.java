package com.example.demo.model.dto;

import com.example.demo.model.enumeration.PizzaCrustEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPizzaCrustDTO {

    private PizzaCrustEnum pizzaCrust;
    private double price;

}
