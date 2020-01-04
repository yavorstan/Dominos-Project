package com.example.demo.model.enumeration;

import lombok.Getter;

@Getter
public enum PizzaSizeEnum {

    //sample price multiplier for different sizes
    SMALL(0.7),
    MEDIUM(1),
    LARGE(1.5);

    private double priceMultiplier;

    PizzaSizeEnum(double priceMultiplier){
        this.priceMultiplier = priceMultiplier;
    }
}
