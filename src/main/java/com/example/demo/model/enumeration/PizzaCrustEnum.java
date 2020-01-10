package com.example.demo.model.enumeration;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum PizzaCrustEnum {

    //sample
    NORMAL(new BigDecimal(0)),
    ITALIAN(new BigDecimal(1.3)),
    PHILADELPHIA(new BigDecimal(2.3));

    private BigDecimal additionalPrice;

    PizzaCrustEnum(BigDecimal additionalPrice){
        this.additionalPrice = additionalPrice;
    }
}
