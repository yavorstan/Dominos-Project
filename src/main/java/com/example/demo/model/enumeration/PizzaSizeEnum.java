package com.example.demo.model.enumeration;

import lombok.Getter;

@Getter
public enum PizzaSizeEnum {

    SMALL(0.7),
    MEDIUM(1),
    LARGE(1.4),
    JUMBO(1.9);

    private double additionalPriceMultiplier;

    PizzaSizeEnum(double additionalPriceMultiplier) {
        this.additionalPriceMultiplier = this.additionalPriceMultiplier;
    }
}
