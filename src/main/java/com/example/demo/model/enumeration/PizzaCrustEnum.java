package com.example.demo.model.enumeration;

import lombok.Getter;

@Getter
public enum PizzaCrustEnum {

    //sample
    NORMAL(0),
    ITALIAN(1.3),
    PHILADELPHIA(2.3);

    private double additionalPrice;

    PizzaCrustEnum(double additionalPrice){
        this.additionalPrice = additionalPrice;
    }
}
