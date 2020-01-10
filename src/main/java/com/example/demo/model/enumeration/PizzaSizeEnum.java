package com.example.demo.model.enumeration;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum PizzaSizeEnum {

    SMALL(new BigDecimal(0.7)),
    MEDIUM(new BigDecimal(1)),
    LARGE(new BigDecimal(1.4)),
    JUMBO(new BigDecimal(1.9));

    private BigDecimal additionalPriceMultiplier;

    PizzaSizeEnum(BigDecimal additionalPriceMultiplier) {
        this.additionalPriceMultiplier = additionalPriceMultiplier;
    }
}
