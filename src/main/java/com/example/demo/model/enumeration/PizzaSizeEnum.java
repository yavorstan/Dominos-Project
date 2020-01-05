package com.example.demo.model.enumeration;

import lombok.Getter;

@Getter
public enum PizzaSizeEnum {

    //sample price addition for different sizes
    SMALL(-1.5),
    MEDIUM(0),
    LARGE(3.6),
    JUMBO(7.1);

    private double additionalPrice;

    PizzaSizeEnum(double additionalPrice) {
        this.additionalPrice = additionalPrice;
    }
}
